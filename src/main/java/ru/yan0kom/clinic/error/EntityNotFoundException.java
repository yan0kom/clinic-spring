package ru.yan0kom.clinic.error;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2979442160266005978L;

    public EntityNotFoundException(Class<?> entityClass, Long entityId) {
        super(String.format("Entity of type %s with id %d not found", entityClass.getName(), entityId));
    }

    public EntityNotFoundException(Class<?> entityClass, String field, String value) {
        super(String.format("Entity of type %s with %s = %s not found", entityClass.getName(), field, value));
    }
}
