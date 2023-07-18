package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.global.response.dto.DataResponse;

import java.time.LocalDateTime;

public record ReadBoardResponse (
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdDate,
        int likeCount,
        int views) implements DataResponse {

    public static ReadBoardResponse newInstance(final Board board) {
        return new ReadBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews()
        );
    }
}
