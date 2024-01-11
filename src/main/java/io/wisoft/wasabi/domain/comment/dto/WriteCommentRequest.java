package io.wisoft.wasabi.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WriteCommentRequest(
        @NotNull Long boardId,
        @NotBlank String content
) {
}
