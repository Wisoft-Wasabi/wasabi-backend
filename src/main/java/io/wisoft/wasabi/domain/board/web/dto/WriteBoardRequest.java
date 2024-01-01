package io.wisoft.wasabi.domain.board.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record WriteBoardRequest(
        @NotBlank String title,
        @NotBlank String content,
        @Nullable String tag,
        @Nullable String[] imageUrls,
        @Nullable List<Long> imageIds
) {
}
