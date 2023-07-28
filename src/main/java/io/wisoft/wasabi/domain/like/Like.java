package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import jakarta.persistence.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private void setBoard(final Board board) {
        this.board = board;
        board.getLikes().add(this);
    }

    private void setMember(final Member member) {
        this.member = member;
        member.getLikes().add(this);
    }

    protected Like() {}

    public Like(
            final Member member,
            final Board board
    ) {
        setMember(member);
        setBoard(board);
    }

    /* getter */
    public Long getId() { return id; }

    public Board getBoard() { return board; }

    public Member getMember() { return member; }
}
