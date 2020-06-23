package ru.yan0kom.clinic.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.yan0kom.clinic.dto.RoleInDto;
import ru.yan0kom.clinic.dto.UserInDto;
import ru.yan0kom.clinic.model.AppRole;
import ru.yan0kom.clinic.model.AppUser;
import ru.yan0kom.clinic.security.Privileges;
import ru.yan0kom.clinic.service.AdminService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("privileges")
    @Secured(Privileges.admin_privileges_read)
    public Set<String> listPrivileges() {
        return adminService.listPrivileges();
    }

    @PostMapping("roles")
    @Secured(Privileges.admin_roles_create)
    public AppRole addRole(@RequestBody RoleInDto roleInDto) {
        return adminService.addRole(roleInDto);
    }

    @GetMapping("roles/{id}")
    @Secured(Privileges.admin_roles_read)
    public AppRole getRole(@PathVariable Long id) {
        return adminService.getRole(id);
    }

    @GetMapping("roles")
    @Secured(Privileges.admin_roles_read)
    public List<AppRole> listRoles() {
        return adminService.listRoles();
    }

    @PutMapping("roles/{id}")
    @Secured(Privileges.admin_roles_update)
    public AppRole updateRole(@PathVariable Long id, @RequestBody RoleInDto roleInDto) {
        return adminService.updateRole(id, roleInDto);
    }

    @DeleteMapping("roles/{id}")
    @Secured(Privileges.admin_roles_delete)
    public void deleteRole(@PathVariable Long id) {
        adminService.deleteRole(id);
    }

    @PostMapping("users")
    @Secured(Privileges.admin_users_create)
    public AppUser addUser(@RequestBody UserInDto userInDto) {
        return adminService.addUser(userInDto);
    }

    @GetMapping("users/{id}")
    @Secured(Privileges.admin_users_read)
    public AppUser getUser(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @GetMapping("users")
    @Secured(Privileges.admin_users_read)
    public List<AppUser> listUsers() {
        return adminService.listUsers();
    }

    @PutMapping("users/{id}")
    @Secured(Privileges.admin_users_update)
    public AppUser updateUser(@PathVariable Long id, @RequestBody UserInDto userInDto) {
        return adminService.updateUser(id, userInDto);
    }

    @DeleteMapping("users/{id}")
    @Secured(Privileges.admin_users_delete)
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
}
