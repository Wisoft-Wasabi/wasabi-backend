package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.like.persistence.AnonymousLike;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.like.persistence.LikeQueryRepository;
import io.wisoft.wasabi.domain.like.web.LikeService;
import io.wisoft.wasabi.domain.like.web.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("anonymousLikeService")
public class AnonymousLikeServiceImpl implements LikeService {

    private final Logger logger = LoggerFactory.getLogger(AnonymousLikeServiceImpl.class);
    private final AnonymousLikeRepository anonymousLikeRepository;
    private final BoardRepository boardRepository;
    private final LikeQueryRepository likeQueryRepository;

    public AnonymousLikeServiceImpl(final AnonymousLikeRepository anonymousLikeRepository,
                                    final BoardRepository boardRepository,
                                    final LikeQueryRepository likeQueryRepository) {
        this.anonymousLikeRepository = anonymousLikeRepository;
        this.boardRepository = boardRepository;
        this.likeQueryRepository = likeQueryRepository;
    }

    @Override
    @Transactional
    public RegisterLikeResponse registerLike(final Long accessId, final RegisterLikeRequest request) {

        final Board board = boardRepository.findById(request.boardId())
            .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        final AnonymousLike anonymousLike = new AnonymousLike(accessId, board);

        anonymousLikeRepository.save(anonymousLike);

        logger.info("[Result] 비회원 {} 의 {} 번 게시글 좋아요 등록", accessId, board.getId());

        return new RegisterLikeResponse(anonymousLike.getId());
    }

    @Override
    @Transactional
    public CancelLikeResponse cancelLike(final Long accessId, final Long boardId) {

        final AnonymousLike anonymousLike = anonymousLikeRepository.findBySessionIdAndBoardId(accessId, boardId)
            .orElseThrow(LikeExceptionExecutor::LikeNotFound);

        anonymousLike.delete();
        anonymousLikeRepository.delete(anonymousLike);

        logger.info("[Result] 비회원 {} 의 {} 번 게시글 좋아요 삭제", accessId, boardId);

        return new CancelLikeResponse(anonymousLike.getId());
    }

    @Override
    public GetLikeResponse getLikeStatus(final Long accessId, final Long boardId) {
        final boolean isExistsBoard = boardRepository.existsById(boardId);
        if (!isExistsBoard) {
            throw BoardExceptionExecutor.BoardNotFound();
        }

        final boolean isLike = anonymousLikeRepository.existsBySessionIdAndBoardId(accessId, boardId);

        final int likeCount = Math.toIntExact(likeQueryRepository.countByBoardId(boardId));

        return new GetLikeResponse(isLike, likeCount);
    }
}
