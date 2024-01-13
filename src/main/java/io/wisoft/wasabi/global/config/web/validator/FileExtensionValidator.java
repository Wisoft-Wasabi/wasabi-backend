package io.wisoft.wasabi.global.config.web.validator;

import io.wisoft.wasabi.global.config.common.Const;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {

    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {

        if (file.isEmpty()) {
            return false;
        }

        final String contentType = file.getContentType();

        return fileVerification(contentType);
    }

    private static boolean fileVerification(final String contentType) {
        return !(contentType == null) && ((contentType.equals(Const.IMAGE_EXTENSION_JPEG)) || (contentType.equals(Const.IMAGE_EXTENSION_PNG)) || (contentType.equals(Const.IMAGE_EXTENSION_JPG)));
    }
}
