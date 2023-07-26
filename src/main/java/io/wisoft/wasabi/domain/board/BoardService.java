package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;

import java.util.List;

public interface BoardService {

    WriteBoardResponse writeBoard(final WriteBoardRequest request, Long memberId);

    ReadBoardResponse readBoard(final Long boardId);

    List<ReadBoardResponse> getSortedBoards(final String sortBy);
}
