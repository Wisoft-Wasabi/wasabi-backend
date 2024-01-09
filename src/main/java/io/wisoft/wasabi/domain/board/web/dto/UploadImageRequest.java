package io.wisoft.wasabi.domain.board.web.dto;

import io.wisoft.wasabi.global.config.web.validator.FileExtension;
import org.springframework.web.multipart.MultipartFile;

public record UploadImageRequest(
        @FileExtension MultipartFile image
) {
}
