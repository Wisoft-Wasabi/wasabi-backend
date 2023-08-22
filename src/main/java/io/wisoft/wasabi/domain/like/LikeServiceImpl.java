package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.board.exception.BoardNotFoundException;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.like.dto.*;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LikeMapper likeMapper;
    private static final Logger logger = LogManager.getLogger(LikeServiceImpl.class);

    public LikeServiceImpl(final LikeRepository likeRepository,
                           final MemberRepository memberRepository,
                           final BoardRepository boardRepository,
                           final LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.likeMapper = likeMapper;
    }

    @Transactional
    public RegisterLikeResponse registerLike(final Long memberId, final RegisterLikeRequest request) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardRepository.findById(request.boardId())
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        final Like like = likeMapper.registerLikeRequestToEntity(member, board);

        likeRepository.save(like);

        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 등록", memberId, board.getId());

        return likeMapper.entityToRegisterLikeResponse(like);
    }

    @Override
    @Transactional
    public CancelLikeResponse cancelLike(final Long memberId, final Long boardId) {
        final Like like = likeRepository.findByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(LikeExceptionExecutor::LikeNotFound);

        like.delete();
        likeRepository.deleteById(like.getId());

        return new CancelLikeResponse(like.getId());
    }

    public GetLikeResponse getLikeStatus(final Long memberId, final Long boardId) {

        final boolean isLike = generateIsLike(memberId, boardId);

        final int likeCount = likeRepository.countByBoardId(boardId);

        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 상태 조회", memberId, boardId);

        return new GetLikeResponse(isLike, likeCount);
    }

    private boolean generateIsLike(final Long memberId, final Long boardId) {
        return likeRepository.existsByMemberIdAndBoardId(memberId, boardId);
    }
}