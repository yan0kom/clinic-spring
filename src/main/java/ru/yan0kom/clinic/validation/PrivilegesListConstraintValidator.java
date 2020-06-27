package ru.yan0kom.clinic.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import ru.yan0kom.clinic.security.Privileges;

@Slf4j
public class PrivilegesListConstraintValidator implements ConstraintValidator<PrivilegesListConstraint, List<String>> {
    @Override
    public boolean isValid(List<String> privileges, ConstraintValidatorContext context) {
        log.debug("Validate {}", StringUtils.join(privileges));
        return privileges != null && Privileges.getPrivilegesSet().containsAll(privileges);
    }
}
