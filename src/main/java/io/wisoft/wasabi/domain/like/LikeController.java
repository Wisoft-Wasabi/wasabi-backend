package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.like.dto.CancelLikeRequest;
import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.global.annotation.LoginRequired;
import io.wisoft.wasabi.global.response.CommonResponse;
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
    public ResponseEntity<CommonResponse> registerLike(@LoginRequired final Long memberId,
                                                       @RequestBody @Valid final RegisterLikeRequest request) {

        final RegisterLikeResponse response = likeService.registerLike(memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> cancelLike(@LoginRequired final Long memberId,
                                                     @RequestBody @Valid final CancelLikeRequest request) {

        final CancelLikeResponse response = likeService.cancelLike(memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
