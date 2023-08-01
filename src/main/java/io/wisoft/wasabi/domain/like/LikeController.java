package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.CommonResponse;
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
                                                     @RequestParam("boardId") @Valid final Long boardId) {

        final CancelLikeResponse response = likeService.cancelLike(memberId, boardId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getLikeStatus(@MemberId final Long memberId,
                                                        @RequestParam("boardId") final Long boardId) {

        final GetLikeResponse response = likeService.getLikeStatus(memberId, boardId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
