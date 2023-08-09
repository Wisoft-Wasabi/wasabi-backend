package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardService<T> {

    WriteBoardResponse writeBoard(final WriteBoardRequest request, Long memberId);

    ReadBoardResponse readBoard(final Long boardId, final Long accessId);

    Slice<SortBoardResponse> getSortedBoards(final String sortBy,final Pageable pageable);

    Slice<MyBoardsResponse> getMyBoards(final Long memberId, final Pageable pageable);

    Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable);
}
