package io.wisoft.wasabi.domain.comment.dto;

import jakarta.validation.constraints.NotNull;

public record WriteCommentRequest(
        @NotNull Long boardId,
        @NotNull String content
) {
}
