package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.board.application.BoardImageServiceImpl;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentResponse;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final Logger logger = LoggerFactory.getLogger(BoardImageServiceImpl.class);
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public CommentServiceImpl(final CommentRepository commentRepository,
                              final MemberRepository memberRepository,
                              final BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    public WriteCommentResponse writeComment(final WriteCommentRequest request, final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardRepository.findById(request.boardId())
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        final Comment comment = CommentMapper.writeCommentRequestToEntity(request, member, board);

        commentRepository.save(comment);

        logger.info("[Result] {}번 회원의 {}번 게시글에 대한 댓글 작성", memberId, board.getId());

        return CommentMapper.entityToWriteCommentResponse(comment);
    }

}
