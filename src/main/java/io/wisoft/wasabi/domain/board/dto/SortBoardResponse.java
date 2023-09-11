package io.wisoft.wasabi.domain.board.dto;

import java.time.LocalDateTime;

public record SortBoardResponse(
        Long id,
        String title,
        String writer,
        LocalDateTime createdAt,
        long likeCount,
        int views) {
}
