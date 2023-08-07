package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardService {

    WriteBoardResponse writeBoard(final WriteBoardRequest request, Long memberId);

    ReadBoardResponse readBoard(final Long boardId);

    Slice<SortBoardResponse> getSortedBoards(final String sortBy, final int page, final int size);

    Slice<MyBoardsResponse> getMyBoards(final Long memberId, final Pageable pageable);

    Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable);
}
