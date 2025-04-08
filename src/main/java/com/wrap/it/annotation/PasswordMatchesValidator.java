package com.wrap.it.annotation;

import com.wrap.it.dto.user.ChangePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

    @Override
    public boolean isValid(ChangePasswordRequest dto, ConstraintValidatorContext context) {
        if (dto.password() == null || dto.repeatPassword() == null) {
            return false;
        }
        return dto.password().equals(dto.repeatPassword());
    }
}
