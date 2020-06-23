package ru.yan0kom.clinic.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Privileges {
    public static final String clinic_patients_create = "clinic.patients.create";
    public static final String clinic_patients_read = "clinic.patients.read";
    public static final String clinic_patients_update = "clinic.patients.update";
    public static final String clinic_patients_delete = "clinic.patients.delete";
    public static final String clinic_doctors_create = "clinic.doctors.create";
    public static final String clinic_doctors_read = "clinic.doctors.read";
    public static final String clinic_doctors_update = "clinic.doctors.update";
    public static final String clinic_doctors_delete = "clinic.doctors.delete";
    public static final String clinic_visits_create = "clinic.visits.create";
    public static final String clinic_visits_create_self = "clinic.visits.create-self";
    public static final String clinic_visits_read = "clinic.visits.read";
    public static final String clinic_visits_update = "clinic.visits.update";
    public static final String clinic_visits_update_self = "clinic.visits.update-self";
    public static final String clinic_visits_delete = "clinic.visits.delete";
    public static final String clinic_visits_delete_self = "clinic.visits.delete-self";
    public static final String admin_privileges_read = "admin.privileges.read";
    public static final String admin_roles_create = "admin.roles.create";
    public static final String admin_roles_read = "admin.roles.read";
    public static final String admin_roles_update = "admin.roles.update";
    public static final String admin_roles_delete = "admin.roles.delete";
    public static final String admin_users_create = "admin.users.create";
    public static final String admin_users_read = "admin.users.read";
    public static final String admin_users_update = "admin.users.update";
    public static final String admin_users_delete = "admin.users.delete";
    
    private static final Set<String> privilegesSet;

    private Privileges() {}

    static {
        privilegesSet = new LinkedHashSet<>();
        privilegesSet.add(clinic_patients_create);
        privilegesSet.add(clinic_patients_read);
        privilegesSet.add(clinic_patients_update);
        privilegesSet.add(clinic_patients_delete);
        privilegesSet.add(clinic_doctors_create);
        privilegesSet.add(clinic_doctors_read);
        privilegesSet.add(clinic_doctors_update);
        privilegesSet.add(clinic_doctors_delete);
        privilegesSet.add(clinic_visits_create);
        privilegesSet.add(clinic_visits_create_self);
        privilegesSet.add(clinic_visits_read);
        privilegesSet.add(clinic_visits_update);
        privilegesSet.add(clinic_visits_update_self);
        privilegesSet.add(clinic_visits_delete);
        privilegesSet.add(clinic_visits_delete_self);
        privilegesSet.add(admin_privileges_read);
        privilegesSet.add(admin_roles_create);
        privilegesSet.add(admin_roles_read);
        privilegesSet.add(admin_roles_update);
        privilegesSet.add(admin_roles_delete);
        privilegesSet.add(admin_users_create);
        privilegesSet.add(admin_users_read);
        privilegesSet.add(admin_users_update);
        privilegesSet.add(admin_users_delete);
    }

    public static Set<String> getPrivilegesSet() {
        return privilegesSet;
    }

    public static Set<GrantedAuthority> getAuthoritySet() {
        return privilegesSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
