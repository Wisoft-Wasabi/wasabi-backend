package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.global.config.common.annotation.Anyone;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final LikeService anonymousLikeService;

    public LikeController(@Qualifier("likeService") final LikeService likeService,
                          @Qualifier("anonymousLikeService") final LikeService anonymousLikeService) {
        this.likeService = likeService;
        this.anonymousLikeService = anonymousLikeService;
    }

    @PostMapping
    public ResponseEntity<Response<RegisterLikeResponse>> registerLike(@Anyone final Long accessId,
                                                                       @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                                       @RequestBody @Valid final RegisterLikeRequest request) {

        final RegisterLikeResponse data = isAuthenticated
            ? likeService.registerLike(accessId, request)
            : anonymousLikeService.registerLike(accessId, request);
        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.LIKE_REGISTER_SUCCESS,
                data
            )
        );
    }

    @DeleteMapping
    public ResponseEntity<Response<CancelLikeResponse>> cancelLike(@Anyone final Long accessId,
                                                                   @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                                   @RequestParam("boardId") @Valid final Long boardId) {

        final CancelLikeResponse data = isAuthenticated
            ? likeService.cancelLike(accessId, boardId)
            : anonymousLikeService.cancelLike(accessId, boardId);
        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.LIKE_CANCEL_SUCCESS,
                data
            )
        );
    }

    @GetMapping
    public ResponseEntity<Response<GetLikeResponse>> getLikeStatus(@Anyone final Long accessId,
                                                                   @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                                   @RequestParam("boardId") final Long boardId) {

        final GetLikeResponse data = isAuthenticated
            ? likeService.getLikeStatus(accessId, boardId)
            : anonymousLikeService.getLikeStatus(accessId, boardId);
        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.GET_LIKE_STATUS_SUCCESS,
                data
            )
        );
    }
}
