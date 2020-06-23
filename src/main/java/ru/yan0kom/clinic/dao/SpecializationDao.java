package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.Specialization;

import java.util.Optional;

public interface SpecializationDao extends CrudRepository<Specialization, Long> {
    Optional<Specialization> findByName(String name);
}
