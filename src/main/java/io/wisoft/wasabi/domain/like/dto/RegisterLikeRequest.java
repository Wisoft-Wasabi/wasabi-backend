package io.wisoft.wasabi.domain.like.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterLikeRequest (
        @NotNull Long boardId
) {
}
