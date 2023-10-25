package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.SortBoardResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;

public class BoardListToSliceMapper {
    public static Slice<SortBoardResponse> createBoardList(final Board... boards) {
        return new SliceImpl<>(Arrays.asList(boards)).map(board -> new SortBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews()
        ));
    }
}
