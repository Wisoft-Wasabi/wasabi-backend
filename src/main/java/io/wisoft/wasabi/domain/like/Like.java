package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = PROTECTED)
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

    public static Like createLike(
            final Member member,
            final Board board) {

        final Like like = new Like();
        like.setMember(member);
        like.setBoard(board);

        return like;
    }
}
