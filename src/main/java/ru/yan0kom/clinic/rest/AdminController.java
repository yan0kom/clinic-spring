package ru.yan0kom.clinic.rest;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.yan0kom.clinic.dto.RoleInDto;
import ru.yan0kom.clinic.dto.UserInDto;
import ru.yan0kom.clinic.model.AppRole;
import ru.yan0kom.clinic.model.AppUser;
import ru.yan0kom.clinic.security.Privileges;
import ru.yan0kom.clinic.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "API to manage users, roles and their privileges")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success")})
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get all available priveleges", description = "Required privelege: "+Privileges.admin_privileges_read, tags = {"admin"})
    @GetMapping("privileges")
    @Secured(Privileges.admin_privileges_read)
    public Set<String> listPrivileges() {
        return adminService.listPrivileges();
    }

    @Operation(summary = "Create role with given priveleges", description = "Required privelege: "+Privileges.admin_roles_create, tags = {"admin"})
    @PostMapping("roles")
    @Secured(Privileges.admin_roles_create)
    public AppRole addRole(@RequestBody RoleInDto roleInDto) {
        return adminService.addRole(roleInDto);
    }

    @Operation(summary = "Get role with given id", description = "Required privelege: "+Privileges.admin_roles_read, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @GetMapping("roles/{id}")
    @Secured(Privileges.admin_roles_read)
    public AppRole getRole(@PathVariable Long id) {
        return adminService.getRole(id);
    }

    @Operation(summary = "Get all roles", description = "Required privelege: "+Privileges.admin_roles_read, tags = {"admin"})
    @GetMapping("roles")
    @Secured(Privileges.admin_roles_read)
    public List<AppRole> listRoles() {
        return adminService.listRoles();
    }

    @Operation(summary = "Update role with given id", description = "Required privelege: "+Privileges.admin_roles_update, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @PutMapping("roles/{id}")
    @Secured(Privileges.admin_roles_update)
    public AppRole updateRole(@PathVariable Long id, @RequestBody RoleInDto roleInDto) {
        return adminService.updateRole(id, roleInDto);
    }

    @Operation(summary = "Delete role with given id", description = "Required privelege: "+Privileges.admin_roles_delete, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @DeleteMapping("roles/{id}")
    @Secured(Privileges.admin_roles_delete)
    public void deleteRole(@PathVariable Long id) {
        adminService.deleteRole(id);
    }

    @Operation(summary = "Create user", description = "Required privelege: "+Privileges.admin_users_create, tags = {"admin"})
    @PostMapping("users")
    @Secured(Privileges.admin_users_create)
    public AppUser addUser(@RequestBody UserInDto userInDto) {
        return adminService.addUser(userInDto);
    }

    @Operation(summary = "Get user with given id", description = "Required privelege: "+Privileges.admin_users_read, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @GetMapping("users/{id}")
    @Secured(Privileges.admin_users_read)
    public AppUser getUser(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @Operation(summary = "Get all users", description = "Required privelege: "+Privileges.admin_users_read, tags = {"admin"})
    @GetMapping("users")
    @Secured(Privileges.admin_users_read)
    public List<AppUser> listUsers() {
        return adminService.listUsers();
    }

    @Operation(summary = "Update user with given id", description = "Required privelege: "+Privileges.admin_users_update, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    @PutMapping("users/{id}")
    @Secured(Privileges.admin_users_update)
    public AppUser updateUser(@PathVariable Long id, @RequestBody UserInDto userInDto) {
        return adminService.updateUser(id, userInDto);
    }

    @Operation(summary = "Delete user with given id", description = "Required privelege: "+Privileges.admin_users_delete, tags = {"admin"})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    @DeleteMapping("users/{id}")
    @Secured(Privileges.admin_users_delete)
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
}
