package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudRepository<AppUser, Long> {
    List<AppUser> findAll();
    Optional<AppUser> findByUsername(String username);
    void flush();
}
