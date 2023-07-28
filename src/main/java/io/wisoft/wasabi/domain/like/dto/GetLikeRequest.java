package io.wisoft.wasabi.domain.like.dto;

import jakarta.validation.constraints.NotNull;

public record GetLikeRequest (
        @NotNull Long boardId,
        boolean isLike,
        int likeCount
) {
}
