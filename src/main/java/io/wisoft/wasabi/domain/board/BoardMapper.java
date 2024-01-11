package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.domain.comment.Comment;
import io.wisoft.wasabi.domain.member.Member;
import org.springframework.data.domain.Slice;

import java.util.Locale;

public class BoardMapper {

    static Board writeBoardRequestToEntity(final WriteBoardRequest request, final Member member) {

        return new Board(
                request.title(),
                request.content(),
                member
        );
    }

    static WriteBoardResponse entityToWriteBoardResponse(final Board board) {

        return new WriteBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName()
        );
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

//    ReadBoardResponse entityToReadBoardResponse(final Board board, final boolean isLike, final Comment comment, final Boolean isBoardWriter) {
//
//        return new ReadBoardResponse(
//                board.getId(),
//                board.getTitle(),
//                board.getContent(),
//                new ReadBoardResponse.Writer(
//                        board.getMember().getEmail(),
//                        board.getMember().getName(),
//                        board.getMember().getReferenceUrl(),
//                        board.getMember().getPart(),
//                        board.getMember().getOrganization(),
//                        board.getMember().getMotto()
//                ),
//                board.getCreatedAt(),
//                board.getLikes().size(),
//                board.getViews(),
//                isLike,
//                String.valueOf(board.getTag()),
//                new ReadBoardResponse.Comment(
//                        comment.getId(),
//                        comment.getContent(),
//                        comment.getMember().getId(),
//                        comment.getMember().getName(),
//                        isBoardWriter,
//                        comment.getCreatedAt()
//                )
//        );
//    }

    static Slice<MyBoardsResponse> entityToMyBoardsResponse(final Slice<Board> myBoards) {

        return myBoards.map(board -> new MyBoardsResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews()
        ));
    }

    static Slice<MyLikeBoardsResponse> entityToMyLikeBoardsResponse(final Slice<Board> boards) {

        return boards.map(board -> new MyLikeBoardsResponse(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getCreatedAt(),
                board.getLikes().size(),
                board.getViews()
        ));
    }
}