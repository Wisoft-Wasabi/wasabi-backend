package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.tag.Tag;
import io.wisoft.wasabi.domain.tag.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final LikeRepository likeRepository;


    public BoardServiceImpl(final BoardRepository boardRepository,
                            final BoardImageRepository boardImageRepository,
                            final MemberRepository memberRepository,
                            final TagRepository tagRepository,
                            final BoardQueryRepository boardQueryRepository,
                            final LikeRepository likeRepository) {
        this.boardRepository = boardRepository;
        this.boardImageRepository = boardImageRepository;
        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.boardQueryRepository = boardQueryRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
    public WriteBoardResponse writeBoard(final WriteBoardRequest request, final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = BoardMapper.writeBoardRequestToEntity(request, member);

        saveTag(board, request.tag());
        boardRepository.save(board);

        mappingBoardAndImage(request, board);

        logger.info("[Result] {}번 회원의 {}번 게시글 작성", memberId, board.getId());

        return BoardMapper.entityToWriteBoardResponse(board);
    }

    private void saveTag(final Board board,
                         final String tagName) {

        if (StringUtils.hasText(tagName)) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            board.setTag(tag);
        }
    }

    private void mappingBoardAndImage(final WriteBoardRequest request, final Board board) {

        final List<BoardImage> images = boardImageRepository.findAllBoardImagesById(request.imageIds());
        images.forEach(image -> image.setBoard(board));

        logger.info("[Result] {}번 게시글과 {}번 이미지 연관관계 매핑", board.getId(), request.imageIds());
    }

    @Override
    @Transactional
    public ReadBoardResponse readBoard(final Long boardId, final Long accessId, final boolean isAuthenticated) {

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        board.increaseView();

        logger.info("[Result] {}번 회원의 {}번 게시글 조회", accessId, boardId);
        return boardQueryRepository.readBoard(boardId, accessId, isAuthenticated);
    }

    /**
     * 기존에 QueryDSL에서 조인을 통해 좋아요 갯수를 가져오던 로직을
     * @Formula를 이용해 조인이 아닌 직접 쿼리로 조회하는 방식으로 바꾼 방법
     */
    @Override
    @Transactional
    public ReadBoardResponse readBoardWithFomula(final Long boardId, final Long accessId, final boolean isAuthenticated) {

        final Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        board.increaseView();

        logger.info("[Result] {}번 회원의 {}번 게시글 조회", accessId, boardId);
        Member member = board.getMember();
        return new ReadBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                new ReadBoardResponse.Writer(
                        member.getEmail(),
                        member.getName(),
                        member.getReferenceUrl(),
                        member.getPart(),
                        member.getOrganization(),
                        member.getMotto()
                ),
                board.getCreatedAt(),
                board.getLikeCount() + board.getAnonymousLikeCount(),
                board.getViews(),
                likeRepository.existsByMemberIdAndBoardId(member.getId(), boardId),
                board.getTag().getName()
        );
    }

    @Override
    public Slice<SortBoardResponse> getBoardList(final String sortBy,
                                                 final Pageable pageable,
                                                 final String keyword) {

        final BoardSortType sortType = validateSortType(sortBy.toUpperCase());

        logger.info("[Result] {}를 기준으로 정렬한 게시글 목록 조회", sortBy);

        return this.boardQueryRepository.boardList(pageable, sortType, keyword);
    }

    private BoardSortType validateSortType(final String sortBy) {
        for (BoardSortType value : BoardSortType.values()) {
            if (value.getSortType().equalsIgnoreCase(sortBy.toUpperCase())) {
                return value;
            }
        }
        throw BoardExceptionExecutor.BoardSortTypeInvalidException();
    }

    @Override
    public Slice<MyBoardsResponse> getMyBoards(final Long memberId, final Pageable pageable) {

        final Slice<Board> myBoards = boardRepository.findAllMyBoards(memberId, pageable);

        logger.info("[Result] {}번 회원의 자신이 작성한 게시글 목록 조회", memberId);

        return BoardMapper.entityToMyBoardsResponse(myBoards);
    }

    @Override
    public Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable) {

        final Slice<Board> myLikeBoards = boardRepository.findAllMyLikeBoards(memberId, pageable);

        logger.info("[Result] {}번 회원의 자신이 좋아요 한 게시글 목록 조회", memberId);

        return BoardMapper.entityToMyLikeBoardsResponse(myLikeBoards);
    }
}
