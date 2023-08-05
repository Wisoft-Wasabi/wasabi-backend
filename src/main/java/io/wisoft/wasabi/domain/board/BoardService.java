package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.SortBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BoardService {

    WriteBoardResponse writeBoard(final WriteBoardRequest request, final Long memberId);

    ReadBoardResponse readBoard(final Long boardId);

    Slice<SortBoardResponse> getSortedBoards(final String sortBy, final Pageable pageable);
}