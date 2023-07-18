package io.wisoft.wasabi.domain.board.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WriteBoardRequest(
        @NotNull Long memberId,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull String[] tags,
        @Nullable String[] imageUrls
) {
}
