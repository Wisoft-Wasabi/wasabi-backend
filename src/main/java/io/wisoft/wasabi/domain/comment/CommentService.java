package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentResponse;

public interface CommentService {
    WriteCommentResponse writeComment(final WriteCommentRequest request, final Long memberId);
}
