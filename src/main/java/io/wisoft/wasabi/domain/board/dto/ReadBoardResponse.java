package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.global.response.dto.DataResponse;

public record ReadBoardResponse (
        Long id,
        String title,
        String content,
        String writer,
        String createdDate,
        int likeCount,
        int views) implements DataResponse {

    public static ReadBoardResponse newInstance(final Board board) {
        return new ReadBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getName(),
                board.getCreatedAt().toString(),
                board.getLikes().size(),
                board.getViews()
        );
    }
}
