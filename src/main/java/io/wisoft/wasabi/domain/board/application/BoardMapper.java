package io.wisoft.wasabi.domain.board.application;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.board.persistence.BoardImage;
import io.wisoft.wasabi.domain.board.web.dto.*;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.springframework.data.domain.Slice;

public class BoardMapper {

    static Board writeBoardRequestToEntity(final WriteBoardRequest request, final Member member) {

        return new Board(
                request.title(),
                request.content(),
                member
        );
    }

    static WriteBoardResponse entityToWriteBoardResponse(final Board board) {

        return new WriteBoardResponse(board.getId());
    }

    static BoardImage uploadImageRequestToEntity(final String fileName, final String storeImagePath) {

        return new BoardImage(
                fileName,
                storeImagePath
        );
    }

    static UploadImageResponse entityToUploadImageResponse(final BoardImage boardImage) {

        return new UploadImageResponse(
                boardImage.getStoreImagePath(),
                boardImage.getId()
        );
    }

    static DeleteImageResponse entityToDeleteImageResponse(final Long imageId) {

        return new DeleteImageResponse(imageId);
    }

    static Slice<MyBoardsResponse> entityToMyBoardsResponse(final Slice<Board> myBoards) {

        return myBoards.map(board -> new MyBoardsResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size() + board.getAnonymousLikes().size(),
                board.getViews()
        ));
    }

    public static Slice<MyLikeBoardsResponse> entityToMyLikeBoardsResponse(final Slice<Board> boards) {

        return boards.map(board -> new MyLikeBoardsResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size() + board.getAnonymousLikes().size(),
                board.getViews()
        ));
    }
}