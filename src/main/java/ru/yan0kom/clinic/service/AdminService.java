package ru.yan0kom.clinic.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.yan0kom.clinic.dao.RoleDao;
import ru.yan0kom.clinic.dao.UserDao;
import ru.yan0kom.clinic.dto.RoleInDto;
import ru.yan0kom.clinic.dto.UserInDto;
import ru.yan0kom.clinic.error.EntityNotFoundException;
import ru.yan0kom.clinic.model.AppRole;
import ru.yan0kom.clinic.model.AppUser;
import ru.yan0kom.clinic.security.Privileges;

@Service
public class AdminService extends BaseService implements UserDetailsService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void setupData() {
        if (userDao.count() > 0) {
            return;
        }

        AppRole role = addRole(new RoleInDto("admin", Arrays.asList(
                Privileges.clinic_doctors_create,
                Privileges.clinic_doctors_read,
                Privileges.clinic_doctors_update,
                Privileges.clinic_doctors_delete,
                Privileges.admin_privileges_read,
                Privileges.admin_roles_create,
                Privileges.admin_roles_read,
                Privileges.admin_roles_update,
                Privileges.admin_roles_delete,
                Privileges.admin_users_create,
                Privileges.admin_users_read,
                Privileges.admin_users_update,
                Privileges.admin_users_delete)));
        addUser(new UserInDto(null, "admin", "admin", role.getId()));

        addRole(new RoleInDto("doctor", Arrays.asList(
                Privileges.clinic_patients_create,
                Privileges.clinic_patients_read,
                Privileges.clinic_patients_update,
                Privileges.clinic_patients_delete,
                Privileges.clinic_doctors_read,
                Privileges.clinic_visits_create_self,
                Privileges.clinic_visits_read,
                Privileges.clinic_visits_update_self,
                Privileges.clinic_visits_delete_self)));
    }

    public Set<String> listPrivileges() {
        return Privileges.getPrivilegesSet();
    }

    public AppRole addRole(RoleInDto roleInDto) {
        return roleDao.save(new AppRole(null, roleInDto.getName(), roleInDto.getPrivileges()));
    }

    public AppRole getRole(Long id) {
        return roleDao.findById(id).orElseThrow(() -> new EntityNotFoundException(AppRole.class, id));
    }

    public AppRole getRole(String name) {
        return roleDao.findByName(name).orElseThrow(() -> new EntityNotFoundException(AppRole.class, "name", name));
    }

    public List<AppRole> listRoles() {
        return roleDao.findAll();
    }

    public AppRole updateRole(Long id, RoleInDto roleInDto) {
        checkExistence(roleDao, AppRole.class, id);
        return roleDao.save(new AppRole(id, roleInDto.getName(), roleInDto.getPrivileges()));
    }

    public void deleteRole(Long id) {
        checkExistence(roleDao, AppRole.class, id);
        roleDao.deleteById(id);
    }

    public AppUser addUser(UserInDto userInDto) {
        return userDao.save(new AppUser(null,
                userInDto.getUsername(),
                passwordEncoder.encode(userInDto.getPassword()),
                getRole(userInDto.getRoleId())));
    }

    public AppUser getUser(Long id) {
        return userDao.findById(id).orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
    }

    public List<AppUser> listUsers() {
        return userDao.findAll();
    }

    public AppUser updateUser(Long id, UserInDto userInDto) {
        checkExistence(userDao, AppUser.class, id);
        return userDao.save(new AppUser(id,
                userInDto.getUsername(),
                passwordEncoder.encode(userInDto.getPassword()),
                getRole(userInDto.getRoleId())));
    }

    public void deleteUser(Long id) {
        checkExistence(userDao, AppUser.class, id);
        userDao.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username).map(appUser ->
                new org.springframework.security.core.userdetails.User(
                        appUser.getUsername(),
                        appUser.getPassword(),
                        appUser.getRole().getPrivileges().stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
