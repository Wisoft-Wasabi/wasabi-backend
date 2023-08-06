package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.used.persistence.Used;
import org.springframework.data.domain.Slice;
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
                board.getViews(),
                false,
                board.getUsages().stream()
                        .map(Used::getTag)
                        .toList()
        );
    }

    public Slice<SortBoardResponse> entityToSortBoardResponse(final Slice<Board> boards) {
        return boards.map(board -> new SortBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews(),
                false
        ));
    }

    public Slice<MyBoardsResponse> entityToMyBoardsResponse(final Slice<Board> myBoards) {
        return myBoards.map(board -> new MyBoardsResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews()
        ));
    }

}
