package io.wisoft.wasabi.domain.board.web.dto;

public record UploadImageResponse(
        String imageUrl,
        Long imageId
) {
}
