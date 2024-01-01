package io.wisoft.wasabi.domain.board.web;

import io.wisoft.wasabi.domain.board.web.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardService {

    WriteBoardResponse writeBoard(final WriteBoardRequest request, final Long memberId);

    ReadBoardResponse readBoard(final Long boardId, final Long accessId, final boolean isAuthenticated);

    Slice<SortBoardResponse> getBoardList(final String sortBy, final Pageable pageable, final String keyword);

    Slice<MyBoardsResponse> getMyBoards(final Long memberId, final Pageable pageable);

    Slice<MyLikeBoardsResponse> getMyLikeBoards(final Long memberId, final Pageable pageable);
}
