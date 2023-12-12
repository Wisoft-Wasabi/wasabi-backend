package io.wisoft.wasabi.global.config.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {

        if (file.isEmpty()) {
            return false;
        }

        final String contentType = file.getContentType();

        return fileVerification(contentType);
    }

    private static boolean fileVerification(final String contentType) {
        if (contentType != null && ((contentType.equals("image/jpeg")) || (contentType.equals("image/png")) || (contentType.equals("image/jpg")))) {
            return true;
        }
        return false;
    }
}
