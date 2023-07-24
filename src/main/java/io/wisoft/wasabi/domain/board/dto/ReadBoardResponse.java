package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.tag.persistence.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record ReadBoardResponse (
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdDate,
        int likeCount,
        int views,
        boolean isLike,
        List<Tag> tags) { }