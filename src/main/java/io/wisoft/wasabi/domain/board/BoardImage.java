package io.wisoft.wasabi.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;


    public void setBoard(final Board board) {
        this.board = board;
        board.getBoardImages().add(this);
    }

    public static BoardImage createBoardImage(
            final String url,
            final Board board) {

        final BoardImage boardImage = new BoardImage();
        boardImage.url = url;
        boardImage.setBoard(board);

        return boardImage;
    }
}