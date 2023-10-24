package io.wisoft.wasabi.domain.board.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WriteBoardRequest(
        @NotBlank String title,
        @NotBlank String content,
        @Nullable String tag,
        @Nullable String[] imageUrls,
        @NotNull List<Long> imageIds
) {
}
