package io.wisoft.wasabi.global.annotation;

import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordCheckValidator implements ConstraintValidator<PasswordCheck, SignupRequest> {

    private String field;
    private String fieldMatch;

    public void initialize(PasswordCheck constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    public boolean isValid(SignupRequest request, ConstraintValidatorContext context) {
        String fieldValue = request.password();
        String fieldMatchValue = request.checkPassword();

        if (fieldValue != null && !fieldValue.equals(fieldMatchValue)) {
            return false;
        }

        return true;
    }
}
