package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardService(final BoardRepository boardRepository,
                        final MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public WriteBoardResponse writeBoard(final WriteBoardRequest request) {

        final Member member = memberRepository.findById(request.memberId())
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = Board.createBoard(
                request.title(),
                request.content(),
                member);

        boardRepository.save(board);
        saveImages(board, request);

        return WriteBoardResponse.newInstance(board);
    }

    private void saveImages(final Board board, final WriteBoardRequest request) {

        final String[] images = request.imageUrls();

        if (images != null) {
            Arrays.stream(images)
                    .map(image -> BoardImage.createBoardImage(image, board))
                    .forEach(boardImage -> board.getBoardImages().add(boardImage));
        }
    }

    public ReadBoardResponse readBoard(final Long boardId) {

        final Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardExceptionExecutor::BoardNotFound);

        return ReadBoardResponse.newInstance(board);
    }
}
