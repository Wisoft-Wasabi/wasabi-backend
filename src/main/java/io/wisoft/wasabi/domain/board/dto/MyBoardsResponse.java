package io.wisoft.wasabi.domain.board.dto;

import java.time.LocalDateTime;

public record MyBoardsResponse(
        Long id,
        String title,
        String writer,
        LocalDateTime createdAt,
        int likeCount,
        int views
) {
}
