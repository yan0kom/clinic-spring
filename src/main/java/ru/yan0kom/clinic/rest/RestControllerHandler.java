package ru.yan0kom.clinic.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ru.yan0kom.clinic.error.EntityNotFoundException;
import ru.yan0kom.clinic.error.ValidationError;

@RestControllerAdvice("ru.yan0kom.clinic.rest")
public class RestControllerHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ValidationError validationError = new ValidationError(StringUtils.substringBefore(ex.getLocalizedMessage(), ":"));
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationError.addFieldError(
                    StringUtils.substringBeforeLast(fieldError.getField(), "."),
                    fieldError.getDefaultMessage());
        }
        /*
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }*/

        return handleExceptionInternal(ex, validationError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        ValidationError validationError = new ValidationError("ConstraintViolationException");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            validationError.addFieldError(
                    StringUtils.substringAfterLast(violation.getPropertyPath().toString(), "."),
                    violation.getMessage());
        }

        return new ResponseEntity<Object>(validationError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
