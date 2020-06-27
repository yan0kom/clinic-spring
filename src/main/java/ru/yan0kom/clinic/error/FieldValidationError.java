package ru.yan0kom.clinic.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FieldValidationError {
    private final String field;
    private final String message;
}
