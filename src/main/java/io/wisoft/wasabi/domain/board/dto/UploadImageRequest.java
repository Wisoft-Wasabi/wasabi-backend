package io.wisoft.wasabi.domain.board.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadImageRequest(
        MultipartFile image
) {
}
