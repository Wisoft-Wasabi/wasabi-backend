package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.global.response.dto.DataResponse;

import java.time.LocalDateTime;

public record ReadBoardResponse (
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdDate,
        int likeCount,
        int views) implements DataResponse { }
