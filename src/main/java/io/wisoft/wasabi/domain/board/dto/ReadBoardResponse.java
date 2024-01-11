package io.wisoft.wasabi.domain.board.dto;

import io.wisoft.wasabi.domain.member.Part;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ReadBoardResponse(
        Long id,
        String title,
        String content,
        Writer writer,
        LocalDateTime createdAt,
        long likeCount,
        int views,
        boolean isLike,
        String tag,
        List<Comment> comments
) {
    public ReadBoardResponse(
            Long id,
            String title,
            String content,
            Writer writer,
            LocalDateTime createdAt,
            long likeCount,
            int views,
            boolean isLike,
            String tag
    ) {
        // 댓글이 없는 경우 null을 리턴하는 것을 방지하기 위해 comments를 Collections.emptyList()로 비어있는 리스트를 전달
        this(id, title, content, writer, createdAt, likeCount, views, isLike, tag, Collections.emptyList());
    }

    public record Writer(
            String email,
            String name,
            String referenceUrl,
            Part part,
            String organization,
            String motto
    ){}

    public record Comment(
            Long id,
            String content,
            Long writerId,
            String writer,
            Boolean isBoardWriter,
            LocalDateTime createdAt
    ){}

    // record 로 선언해서 값을 바꾸지 못하기 때문에 withComments시 댓글을 저장하도록 구현
    public ReadBoardResponse addComments(List<Comment> comments) {
        return new ReadBoardResponse(
                id, title, content, writer, createdAt, likeCount, views, isLike, tag, List.copyOf(comments)
        );
    }

}