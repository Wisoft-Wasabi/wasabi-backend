package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.domain.tag.persistence.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record ReadBoardResponse(
        Long id,
        String title,
        String content,
        Writer writer,
        LocalDateTime createdAt,
        int likeCount,
        int views,
        boolean isLike,
        List<Tag> tags) {

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