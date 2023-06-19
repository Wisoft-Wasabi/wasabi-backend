package io.wisoft.wasabi.domain.usage.persistence;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.tag.persistence.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Usage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Board board;

    @JoinColumn(name = "tag_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Tag tag;

    public void setBoard(final Board board) {
        this.board = board;
        board.getUsages().add(this);
    }

    public void setTag(final Tag tag) {
        this.tag = tag;
        tag.getUsages().add(this);
    }

    public static Usage create(final Board board, final Tag tag) {
        final Usage usage = new Usage();
        usage.setBoard(board);
        usage.setTag(tag);

        return usage;
    }
}