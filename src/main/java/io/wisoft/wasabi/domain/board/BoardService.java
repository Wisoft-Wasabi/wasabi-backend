package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;

public interface BoardService {

    WriteBoardResponse writeBoard(final WriteBoardRequest request);

    ReadBoardResponse readBoard(final Long boardId);
}
