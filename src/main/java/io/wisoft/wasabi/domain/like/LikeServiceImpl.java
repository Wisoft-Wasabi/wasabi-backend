package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardNotFoundException;
import io.wisoft.wasabi.domain.like.dto.CancelLikeRequest;
import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LikeServiceImpl implements LikeService {
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
                .orElseThrow(MemberNotFoundException::new);

        final Board board = boardRepository.findById(request.boardId())
                .orElseThrow(BoardNotFoundException::new);

        final Like like = likeMapper.registerLikeRequestToEntity(member, board);

        likeRepository.save(like);

        return likeMapper.entityToRegisterLikeResponse(like);
    }

    @Override
    @Transactional
    public CancelLikeResponse cancelLike(final Long memberId, final CancelLikeRequest request) {
        final Like like = likeRepository.findByMemberIdAndBoardId(memberId, request.boardId())
                .orElseThrow(LikeExceptionExecutor::LikeNotFound);

        likeRepository.deleteById(like.getId());

        return new CancelLikeResponse(like.getId());
    }
}