package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.Patient;

import java.util.List;

public interface PatientDao extends CrudRepository<Patient, Long> {
    List<Patient> findAll();
}
