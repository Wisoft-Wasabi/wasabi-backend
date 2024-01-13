package io.wisoft.wasabi.domain.like.web.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterLikeRequest (
        @NotNull Long boardId
) {
}
