package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.member.Part;

import java.time.LocalDateTime;

public record ReadBoardResponse(
        Long id,
        String title,
        String content,
        Writer writer,
        LocalDateTime createdAt,
        long likeCount,
        int views,
        boolean isLike,
        String tag) {

    public record Writer(
            String email,
            String name,
            String referenceUrl,
            Part part,
            String organization,
            String motto
    ) {
    }
}