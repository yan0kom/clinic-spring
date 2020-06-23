package ru.yan0kom.clinic.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.model.Visit;

import java.util.List;

public interface VisitDao extends CrudRepository<Visit, Long> {
    List<Visit> findAll();
}
