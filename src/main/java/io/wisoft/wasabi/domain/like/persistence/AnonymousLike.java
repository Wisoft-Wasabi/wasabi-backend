package io.wisoft.wasabi.domain.like.persistence;

import io.wisoft.wasabi.domain.board.persistence.Board;
import jakarta.persistence.*;

@Entity
@Table(name = "anonymous_likes")
public class AnonymousLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    protected AnonymousLike() {}

    public AnonymousLike(
        final Long sessionId,
        final Board board
    ) {
        this.sessionId = sessionId;
        setBoard(board);
    }

    private void setBoard(final Board board) {
        this.board = board;
        board.addAnonymousLike(this);
    }

    public Long getId() {
        return this.id;
    }

    public void delete() {
        this.board.removeAnonymousLike(this);
    }
}
