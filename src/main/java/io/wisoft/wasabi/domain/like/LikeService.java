package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.*;

public interface LikeService {
    RegisterLikeResponse registerLike(final Long memberId, final RegisterLikeRequest request);
    CancelLikeResponse cancelLike(final Long memberId, final CancelLikeRequest request);
    GetLikeResponse getLikeStatus(final Long memberId, final Long boardId);
}
