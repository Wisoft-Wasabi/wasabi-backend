package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;

public interface LikeService {
    RegisterLikeResponse registerLike(final Long accessId, final RegisterLikeRequest request);
    CancelLikeResponse cancelLike(final Long memberId, final Long boardId);
    GetLikeResponse getLikeStatus(final Long memberId, final Long boardId);
}
