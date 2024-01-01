package io.wisoft.wasabi.domain.like.web;

import io.wisoft.wasabi.domain.like.web.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;

public interface LikeService {
    RegisterLikeResponse registerLike(final Long accessId, final RegisterLikeRequest request);
    CancelLikeResponse cancelLike(final Long memberId, final Long boardId);
    GetLikeResponse getLikeStatus(final Long memberId, final Long boardId);
}
