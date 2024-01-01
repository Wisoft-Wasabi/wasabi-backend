package io.wisoft.wasabi.domain.like.web.dto;

import jakarta.validation.constraints.NotNull;

public record CancelLikeRequest(
        @NotNull Long boardId
) {
}
