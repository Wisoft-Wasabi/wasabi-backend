package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.basetime.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storeImagePath;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    protected BoardImage() {
    }

    public BoardImage(
            final String fileName,
            final String storeImagePath) {
        this.fileName = fileName;
        this.storeImagePath = storeImagePath;
    }

    void setBoard(final Board board) {
        this.board = board;
        board.getBoardImages().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStoreImagePath() {
        return storeImagePath;
    }

    public Board getBoard() {
        return board;
    }
}