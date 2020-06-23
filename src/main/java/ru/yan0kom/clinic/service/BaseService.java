package ru.yan0kom.clinic.service;

import org.springframework.data.repository.CrudRepository;
import ru.yan0kom.clinic.error.EntityNotFoundException;

public class BaseService {
    protected <T> void checkExistence(CrudRepository<T, Long> dao, Class<T> entityClass, Long id) {
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException(entityClass, id);
        }
    }
}
