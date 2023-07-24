package io.wisoft.wasabi.domain.board.dto;

// TODO: 응답값 상의 필요
public record WriteBoardResponse (
        Long id,
        String title,
        String writer) { }
