package ru.yan0kom.clinic.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yan0kom.clinic.dao.RoleDao;
import ru.yan0kom.clinic.dao.UserDao;
import ru.yan0kom.clinic.dto.UserInDto;

public class UserInDtoConstraintValidator implements ConstraintValidator<UserInDtoConstraint, UserInDto> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    private boolean hasConstraintViolation = false;
    private ValidationPurpose validationPurpose;

    @Override
    public void initialize(UserInDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        validationPurpose = constraintAnnotation.purpose();
    }

    @Override
    public boolean isValid(UserInDto user, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (validationPurpose == ValidationPurpose.UPDATE) {
            if (user.getId() == null) {
                addConstraintViolation(context, "id", "{javax.validation.constraints.NotNull.message}");
            } else if (!userDao.existsById(user.getId())) {
                addConstraintViolation(context, "id", "{constraints.EntityNotFound.message}");
            }
        }

        if (StringUtils.isBlank(user.getUsername())) {
            addConstraintViolation(context, "username", "{javax.validation.constraints.NotBlank.message}");
        } else if (user.getUsername().length() > 100) {
            addConstraintViolation(context, "username", "{constraints.MaxLength.message} 100");
        } else if (validationPurpose == ValidationPurpose.CREATE && userDao.findByUsername(user.getUsername()).isPresent()) {
            addConstraintViolation(context, "username", "{constraints.Unique.message}");
        } else if (validationPurpose == ValidationPurpose.UPDATE &&
                (userDao.findById(user.getId()).get().getUsername().equals(user.getUsername()) ||
                        !userDao.findByUsername(user.getUsername()).isPresent())) {
            addConstraintViolation(context, "username", "{constraints.Unique.message}");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            addConstraintViolation(context, "password", "{javax.validation.constraints.NotBlank.message}");
        } else if (user.getPassword().length() < 5) {
            addConstraintViolation(context, "password", "{constraints.MinLength.message} 5");
        } else if (user.getPassword().length() > 50) { //BCrypt has limit
            addConstraintViolation(context, "password", "{constraints.MaxLength.message} 50");
        }

        if (user.getRoleId() == null) {
            addConstraintViolation(context, "roleId", "{javax.validation.constraints.NotNull.message}");
        } else if (!roleDao.existsById(user.getRoleId())) {
            addConstraintViolation(context, "roleId", "{constraints.EntityNotFound.message}");
        }

        return !hasConstraintViolation;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String field, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field).addConstraintViolation();
        hasConstraintViolation = true;
    }
}
