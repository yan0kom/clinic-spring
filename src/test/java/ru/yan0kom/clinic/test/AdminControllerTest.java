package ru.yan0kom.clinic.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.yan0kom.clinic.dto.RoleInDto;
import ru.yan0kom.clinic.dto.UserInDto;
import ru.yan0kom.clinic.model.AppRole;
import ru.yan0kom.clinic.model.AppUser;
import ru.yan0kom.clinic.security.Privileges;
import ru.yan0kom.clinic.service.AdminService;

public class AdminControllerTest extends ControllerTestBase {
    @Autowired
    AdminService adminService;

    private String adminToken;
    private String doctorToken;

    @BeforeAll
    public void setup() throws Exception {
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        adminToken = fromRequest(
                post("/oauth/token")
                        .param("grant_type", "password")
                        .param("username", "admin")
                        .param("password", "admin")
                        .with(httpBasic(clientId, clientSecret)),
                typeRef).get("access_token");

        createUser("doctor-1", "doctor-1-pass", adminService.getRole("doctor").getId());
        doctorToken = fromRequest(
                post("/oauth/token")
                        .param("grant_type", "password")
                        .param("username", "doctor-1")
                        .param("password", "doctor-1-pass")
                        .with(httpBasic(clientId, clientSecret)),
                typeRef).get("access_token");
    }

    @Test
    public void whenUnauthorized_thenFail() throws Exception {
        mvc.perform(get("/api/admin/privileges")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/admin/roles")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/admin/roles/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/admin/roles")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/admin/roles/1")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/admin/roles/1")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/admin/users")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/admin/users/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/admin/users")).andExpect(status().isUnauthorized());
        mvc.perform(put("/api/admin/users/1")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/admin/users/1")).andExpect(status().isUnauthorized());
    }

    @Test
    public void whenNoPrivilege_thenFail() throws Exception {
        RoleInDto roleInDto = new RoleInDto("dummy", Arrays.asList("dummy"));
        UserInDto userInDto = new UserInDto("dummy", "dummy", -1L);
        mvcPerform(get("/api/admin/privileges"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(setBody(post("/api/admin/roles"), roleInDto), doctorToken)
                .andExpect(status().isForbidden());
        mvcPerform(get("/api/admin/roles/1"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(get("/api/admin/roles"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(setBody(put("/api/admin/roles/1"), roleInDto), doctorToken)
                .andExpect(status().isForbidden());
        mvcPerform(delete("/api/admin/roles/1"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(setBody(post("/api/admin/users"), userInDto), doctorToken)
                .andExpect(status().isForbidden());
        mvcPerform(get("/api/admin/users/1"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(get("/api/admin/users"), doctorToken).andExpect(status().isForbidden());
        mvcPerform(setBody(put("/api/admin/users/1"), userInDto), doctorToken)
                .andExpect(status().isForbidden());
        mvcPerform(delete("/api/admin/users/1"), doctorToken).andExpect(status().isForbidden());
    }

    @Test
    public void listPriveleges_Success() throws Exception {
        List<?> privelegesList = fromRequest(addToken(get("/api/admin/privileges"), adminToken), List.class);
        assertEquals(Privileges.getAllPrivileges().size(), privelegesList.size());
    }

    @Test
    public void complexTest() throws Exception {
        //create some roles
        AppRole r1 = createRole("Главный врач", Arrays.asList(
                Privileges.clinic_doctors_create,
                Privileges.clinic_doctors_read,
                Privileges.clinic_doctors_update,
                Privileges.clinic_doctors_delete));
        AppRole r2 = createRole("Пациент", Arrays.asList(Privileges.clinic_doctors_read));
        //get existing role
        mvc.perform(addToken(get("/api/admin/roles/{id}", r1.getId()), adminToken))
                .andExpect(status().isOk());
        //get not existing role
        mvc.perform(addToken(get("/api/admin/roles/-1"), adminToken)).andExpect(status().isNotFound());
        //list all roles
        assertEquals(4,
                fromRequest(addToken(get("/api/admin/roles"), adminToken), List.class).size());
        //update existing role
        RoleInDto roleInDto = new RoleInDto("Главврач", r1.getPrivileges());
        r1 = fromRequest(
                addToken(
                        setBody(put("/api/admin/roles/{id}", r1.getId()), roleInDto),
                        adminToken),
                AppRole.class);
        assertEquals(roleInDto.getName(), r1.getName());
        //update not existing role
        mvc.perform(setBody(addToken(put("/api/admin/roles/-1"), adminToken), roleInDto))
                .andExpect(status().isNotFound());
        //delete existing role
        mvc.perform(addToken(delete("/api/admin/roles/{id}", r2.getId()), adminToken))
                .andExpect(status().isOk());
        assertEquals(3,
                fromRequest(addToken(get("/api/admin/roles"), adminToken), List.class).size());
        //delete not existing role
        mvc.perform(addToken(delete("/api/admin/roles/{id}", r2.getId()), adminToken))
                .andExpect(status().isNotFound());
    }

    private AppRole createRole(String name, List<String> privileges) throws Exception {
        RoleInDto body = new RoleInDto(name, privileges);
        AppRole role = fromRequest(addToken(setBody(post("/api/admin/roles"), body), adminToken), AppRole.class);
        assertNotNull(role.getId());
        assertEquals(name, role.getName());
        assertArrayEquals(privileges.toArray(), role.getPrivileges().toArray());
        return role;
    }

    private AppUser createUser(String username, String password, Long roleId) throws Exception {
        UserInDto body = new UserInDto(username, password, roleId);
        AppUser user = fromRequest(addToken(setBody(post("/api/admin/users"), body), adminToken), AppUser.class);
        assertNotNull(user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(roleId, user.getRole().getId());
        return user;
    }
}
