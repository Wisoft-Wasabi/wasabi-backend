package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.global.response.dto.DataResponse;

// TODO: 응답값 상의 필요
public record WriteBoardResponse (
        Long id,
        String title,
        String writer) implements DataResponse {

    public static WriteBoardResponse newInstance(final Board board) {

        return new WriteBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName()
        );
    }
}
