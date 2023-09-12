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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {

    private final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LikeMapper likeMapper;

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
        logger.debug("[{}-좋아요] 조회", like.getId());

        like.delete();
        likeRepository.deleteById(like.getId());
        logger.info("[{}-회원]의 [{}-게시글]에 대한 좋아요 삭제", memberId, boardId);

        logger.info("[{}-회원]의 [{}-게시글]에 대한 좋아요 삭제", memberId, boardId);

        return new CancelLikeResponse(like.getId());
    }

    public GetLikeResponse getLikeStatus(final Long memberId, final Long boardId) {

        final boolean isExistsBoard = boardRepository.existsById(boardId);
        if (!isExistsBoard) {
            throw BoardExceptionExecutor.BoardNotFound();
        }

        final boolean isLike = generateIsLike(memberId, boardId);

        final int likeCount = likeRepository.countByBoardId(boardId);

        logger.info("[Result] 회원 {} 의 {} 번 게시물 좋아요 상태 조회", memberId, boardId);

        return new GetLikeResponse(isLike, likeCount);
    }

    private boolean generateIsLike(final Long memberId, final Long boardId) {
        return likeRepository.existsByMemberIdAndBoardId(memberId, boardId);
    }
}