package io.wisoft.wasabi.global.config.common.annotation;

import io.wisoft.wasabi.domain.auth.dto.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordCheckValidator implements ConstraintValidator<PasswordCheck, SignupRequest> {

    private String field;
    private String fieldMatch;

    public void initialize(final PasswordCheck constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    public boolean isValid(final SignupRequest request, final ConstraintValidatorContext context) {
        final String fieldValue = request.password();
        final String fieldMatchValue = request.checkPassword();

        if (fieldValue != null && !fieldValue.equals(fieldMatchValue)) {
            return false;
        }

        return true;
    }
}
