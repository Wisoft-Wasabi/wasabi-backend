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

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl<T> implements BoardService<T> {

    private final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final BoardMapper boardMapper;


    public BoardServiceImpl(final BoardRepository boardRepository,
                            final MemberRepository memberRepository,
                            final LikeRepository likeRepository,
                            final TagRepository tagRepository,
                            final BoardQueryRepository boardQueryRepository,
                            final BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.tagRepository = tagRepository;
        this.boardQueryRepository = boardQueryRepository;
        this.boardMapper = boardMapper;
    }


    @Transactional
    public WriteBoardResponse writeBoard(final WriteBoardRequest request, final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardMapper.writeBoardRequestToEntity(request, member);

        saveTag(board, request.tag());

        boardRepository.save(board);
        saveImages(board, request);

        logger.info("[Result] {}번 회원의 {}번 게시글 작성", memberId, board.getId());

        return boardMapper.entityToWriteBoardResponse(board);
    }

    private void saveTag(final Board board,
                         final String tagName) {

        if (StringUtils.hasText(tagName)) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            board.setTag(tag);
        }
    }

    private void saveImages(final Board board, final WriteBoardRequest request) {

        final String[] images = request.imageUrls();

        if (images != null) {
            Arrays.stream(images)
                    .map(image -> BoardImage.createBoardImage(image, board))
                    .toList();
        }
    }

    @Transactional
    public ReadBoardResponse readBoard(final Long boardId, final Long accessId) {

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        /**
         * TODO: 비회원일 경우 처리해주기
         */

        // 회원일 경우
        final boolean isLike = likeRepository.findByMemberIdAndBoardId((Long) accessId, boardId).isPresent();
        board.increaseView();

        logger.info("[Result] {}번 회원의 {}번 게시글 조회", accessId, boardId);
        return boardMapper.entityToReadBoardResponse(board, isLike);
        
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

        return boardMapper.entityToMyBoardsResponse(myBoards);
    }

    @Override
    public Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable) {

        final Slice<Board> myLikeBoards = boardRepository.findAllMyLikeBoards(memberId, pageable);

        logger.info("[Result] {}번 회원의 자신이 좋아요 한 게시글 목록 조회", memberId);

        return boardMapper.entityToMyLikeBoardsResponse(myLikeBoards);
    }
}
