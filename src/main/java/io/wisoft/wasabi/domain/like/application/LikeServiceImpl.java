package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.like.web.LikeService;
import io.wisoft.wasabi.domain.like.web.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.like.persistence.LikeQueryRepository;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("likeService")
public class LikeServiceImpl implements LikeService {

    private final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LikeQueryRepository likeQueryRepository;

    public LikeServiceImpl(final LikeRepository likeRepository,
                           final MemberRepository memberRepository,
                           final BoardRepository boardRepository,
                           final LikeQueryRepository likeQueryRepository) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.likeQueryRepository = likeQueryRepository;
    }

    @Override
    @Transactional
    public RegisterLikeResponse registerLike(final Long accessId, final RegisterLikeRequest request) {

        final Member member = memberRepository.findById(accessId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardRepository.findById(request.boardId())
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        if (isAlreadyLiked(member.getId(), board.getId())) {
            throw LikeExceptionExecutor.ExistLike();
        }

        final Like like = LikeMapper.registerLikeRequestToEntity(member, board);

        likeRepository.save(like);

        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 등록", accessId, board.getId());

        return LikeMapper.entityToRegisterLikeResponse(like);
    }

    @Override
    @Transactional
    public CancelLikeResponse cancelLike(final Long memberId, final Long boardId) {
        final Like like = likeRepository.findByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(LikeExceptionExecutor::LikeNotFound);

        like.delete();
        likeRepository.deleteById(like.getId());
        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 삭제", memberId, boardId);

        return new CancelLikeResponse(like.getId());
    }

    public GetLikeResponse getLikeStatus(final Long memberId, final Long boardId) {

        final boolean isExistsBoard = boardRepository.existsById(boardId);
        if (!isExistsBoard) {
            throw BoardExceptionExecutor.BoardNotFound();
        }

        final boolean isLike = isAlreadyLiked(memberId, boardId);

        final int likeCount = Math.toIntExact(likeQueryRepository.countByBoardId(boardId));

        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 상태 조회", memberId, boardId);

        return new GetLikeResponse(isLike, likeCount);
    }

    /**
     * 메서드 사용처 <br/>
     * 1. 게시글 상세 조회시 좋아요 여부 (조회 결과가 없으면, 빈 하트로 화면에 출력) <br/>
     * 2. 특정 게시글에 회원이 좋아요 등록시 중복 여부 확인 (조회 결과가 있으면 중복)
     */
    private boolean isAlreadyLiked(final Long memberId, final Long boardId) {
        return likeRepository.existsByMemberIdAndBoardId(memberId, boardId);
    }
}