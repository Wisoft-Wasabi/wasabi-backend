package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.basetime.BaseTimeEntity;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "writer_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;


    public Comment(
            final String content,
            final Member member,
            final Board board) {
        this.content = content;
        setMember(member);
        setBoard(board);
    }

    protected Comment() {

    }

    private void setMember(final Member member) {
        this.member = member;
        member.addComment(this);
    }

    private void setBoard(final Board board) {
        this.board = board;
        board.addComment(this);
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }
}