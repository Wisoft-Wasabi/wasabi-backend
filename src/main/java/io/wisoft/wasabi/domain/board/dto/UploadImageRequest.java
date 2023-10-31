package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.global.config.common.annotation.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public record UploadImageRequest(
        @ValidFile MultipartFile image
) {
}
