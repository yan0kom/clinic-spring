package ru.yan0kom.clinic.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Priveleges list must not be null and must contains only available priveleges
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = PrivilegesListConstraintValidator.class)
public @interface PrivilegesListConstraint {
    String message() default "{constraints.PrivilegesList.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
