package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentResponse;
import io.wisoft.wasabi.global.config.web.resolver.MemberId;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Response<WriteCommentResponse>> writeComment(
            @RequestBody @Valid final WriteCommentRequest request,
            @MemberId final Long memberId) {

        final WriteCommentResponse data = commentService.writeComment(request, memberId);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.COMMENT_WRITE_SUCCESS,
                        data
                )
        );
    }
}
