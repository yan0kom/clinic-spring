package ru.yan0kom.clinic.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ru.yan0kom.clinic.security.Privileges;

public class PrivilegesListConstraintValidator implements ConstraintValidator<PrivilegesListConstraint, List<String>> {
    @Override
    public boolean isValid(List<String> privileges, ConstraintValidatorContext context) {
        return privileges != null && Privileges.getPrivilegesSet().containsAll(privileges);
    }
}
