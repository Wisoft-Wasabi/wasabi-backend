package io.wisoft.wasabi.domain.like.dto;

public record GetLikeResponse (
        boolean isLike,
        int likeCount
) {
}
