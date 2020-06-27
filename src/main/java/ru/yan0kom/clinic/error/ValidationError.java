package ru.yan0kom.clinic.error;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValidationError {
    private final String error = getClass().getSimpleName();
    private final String message;
    private final List<FieldValidationError> errors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        errors.add(new FieldValidationError(field, message));
    }
}
