package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.*;
import io.wisoft.wasabi.global.config.common.annotation.Anyone;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(final LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Response<?>> registerLike(@Anyone final Object accessId,
                                                    @RequestBody @Valid final RegisterLikeRequest request) {

        // accessId = sessionId 일 경우 -> 비회원 좋아요
        if (accessId instanceof String) {
            final RegisterAnonymousLikeResponse data = likeService.registerAnonymousLike((String) accessId, request);
            return ResponseEntity.ofNullable(
                    Response.of(
                            ResponseType.LIKE_REGISTER_SUCCESS,
                            data
                    )
            );
        }

        // accessId = memberId 일 경우 -> 회원 좋아요
        final RegisterLikeResponse data = likeService.registerLike((Long) accessId, request);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.LIKE_REGISTER_SUCCESS,
                        data
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Response<CancelLikeResponse>> cancelLike(@MemberId final Long memberId,
                                                                   @RequestParam("boardId") @Valid final Long boardId) {

        final CancelLikeResponse data = likeService.cancelLike(memberId, boardId);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.LIKE_CANCEL_SUCCESS,
                        data
                )
        );
    }

    @GetMapping
    public ResponseEntity<Response<GetLikeResponse>> getLikeStatus(@MemberId final Long memberId,
                                                                   @RequestParam("boardId") final Long boardId) {

        final GetLikeResponse data = likeService.getLikeStatus(memberId, boardId);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.GET_LIKE_STATUS_SUCCESS,
                        data
                )
        );
    }
}
