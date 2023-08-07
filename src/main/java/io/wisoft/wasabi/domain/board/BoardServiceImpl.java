package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl<T> implements BoardService<T> {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final BoardMapper boardMapper;

    public BoardServiceImpl(final BoardRepository boardRepository,
                            final MemberRepository memberRepository,
                            final LikeRepository likeRepository,
                            final BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.boardMapper = boardMapper;
    }


    @Transactional
    public WriteBoardResponse writeBoard(final WriteBoardRequest request, final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardMapper.writeBoardRequestToEntity(request, member);

        boardRepository.save(board);
        saveImages(board, request);

        return boardMapper.entityToWriteBoardResponse(board);
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
    public ReadBoardResponse readBoard(final Long boardId, final T accessId) {

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        /**
         * TODO: 비회원일 경우 처리해주기
         */

        // 회원일 경우
        final boolean isLike = likeRepository.findByMemberIdAndBoardId((Long) accessId, boardId).isPresent();
        board.increaseView();

        return boardMapper.entityToReadBoardResponse(board, isLike);
    }

    @Override
    public Slice<SortBoardResponse> getSortedBoards(final String sortBy, final int page, final int size) {
        // 음수일 경우를 방지
        final int validatedPage = Math.max(0, page);
        final int validatedSize = Math.max(2, size);

        final BoardSortType sortType = validateSortType(sortBy.toUpperCase());
        final Slice<Board> boardSlice = sort(sortType, validatedPage, validatedSize);

        return boardMapper.entityToSortBoardResponse(boardSlice);
    }

    private Slice<Board> sort(final BoardSortType sortType, final int page, final int size) {
        return switch (sortType) {
            case LATEST -> boardRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
            case VIEWS -> boardRepository.findAllByOrderByViewsDesc(PageRequest.of(page, size));
            case LIKES -> boardRepository.findAllByOrderByLikesDesc(PageRequest.of(page, size));
            case DEFAULT -> boardRepository.findDefaultBoards(PageRequest.of(page, size));
            default -> throw BoardExceptionExecutor.BoardSortTypeInvalidException();
        };
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
    public Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable) {

        final Slice<Board> myLikeBoards = boardRepository.findAllMyLikeBoards(memberId, pageable);
        return boardMapper.entityToMyLikeBoardsResponse(myLikeBoards);
    }
}
