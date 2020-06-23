package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.AppRole;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends CrudRepository<AppRole, Long> {
    List<AppRole> findAll();
    Optional<AppRole> findByName(String name);
}
