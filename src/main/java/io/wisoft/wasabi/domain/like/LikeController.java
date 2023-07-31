package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.*;
import io.wisoft.wasabi.global.annotation.LoginRequired;
import io.wisoft.wasabi.global.annotation.MemberId;
import io.wisoft.wasabi.global.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(final LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> registerLike(@MemberId final Long memberId,
                                                       @RequestBody @Valid final RegisterLikeRequest request) {

        final RegisterLikeResponse response = likeService.registerLike(memberId, request);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> cancelLike(@MemberId final Long memberId,
                                                     @RequestBody @Valid final CancelLikeRequest request) {

        final CancelLikeResponse response = likeService.cancelLike(memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getLikeStatus(@LoginRequired final Long memberId,
                                                        @RequestParam("boardId") final Long boardId) {

        final GetLikeResponse response = likeService.getLikeStatus(memberId, boardId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
