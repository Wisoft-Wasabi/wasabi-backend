package io.wisoft.wasabi.domain.board.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record WriteBoardRequest(
        @NotBlank String title,
        @NotBlank String content,
        @Nullable String tag,
        @Nullable String[] imageUrls
) {
}
