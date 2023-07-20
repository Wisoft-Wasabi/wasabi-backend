package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    Board writeBoardRequestToEntity(final WriteBoardRequest request, final Member member) {

        return Board.createBoard(
                request.title(),
                request.content(),
                member);
    }


    WriteBoardResponse entityToWriteBoardResponse(final Board board) {

        return new WriteBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName()
        );
    }


    ReadBoardResponse entityToReadBoardResponse(final Board board) {

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