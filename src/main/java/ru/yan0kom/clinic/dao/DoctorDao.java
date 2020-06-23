package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.Doctor;

import java.util.List;

public interface DoctorDao extends CrudRepository<Doctor, Long> {
    List<Doctor> findAll();
}
