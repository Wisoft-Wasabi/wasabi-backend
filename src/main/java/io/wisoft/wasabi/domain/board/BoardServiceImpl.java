package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardMapper boardMapper;
    private final Map<String, Function<Sort, List<Board>>> sortMap;

    public BoardServiceImpl(final BoardRepository boardRepository,
                            final MemberRepository memberRepository,
                            final BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.boardMapper = boardMapper;
        this.sortMap = createSortingMap();
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
    public ReadBoardResponse readBoard(final Long boardId) {

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        board.increaseView();

        return boardMapper.entityToReadBoardResponse(board);
    }

    @Override
    public List<ReadBoardResponse> getSortedBoards(final String sortBy) {
        final Function<Sort, List<Board>> sortFunction = sortMap.get(sortBy);
        // 방식이 확정된다면 BusinessException로 처리예정
        if (sortFunction == null) {
            throw new IllegalArgumentException("잘못된 sortBy 값: " + sortBy);
        }


        final List<Board> sortedBoards = sortFunction.apply(Sort.unsorted());

        return boardMapper.entityToReadBoardResponse(sortedBoards);
    }

    private Map<String, Function<Sort, List<Board>>> createSortingMap() {
        final Map<String, Function<Sort, List<Board>>> sortMap = new HashMap<>();
        sortMap.put("createdAt", sort -> boardRepository.findAllByOrderByCreatedAtDesc());
        sortMap.put("views", sort -> boardRepository.findAllByOrderByViewsDesc());
        sortMap.put("likes", sort -> boardRepository.findAllByOrderByLikesDesc());

        return sortMap;
    }
}
