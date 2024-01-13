package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;

public class CommentMapper {

    static Comment writeCommentRequestToEntity(final WriteCommentRequest request, final Member member, final Board board) {

        return new Comment(
                request.content(),
                member,
                board
        );
    }

    static WriteCommentResponse entityToWriteCommentResponse(final Comment comment){

        return new WriteCommentResponse(
                comment.getId(),
                comment.getBoard().getId()
        );
    }
}
