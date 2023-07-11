package io.wisoft.wasabi.domain.board.persistence;

import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.usage.persistence.Usage;
import io.wisoft.wasabi.global.basetime.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany(mappedBy = "board")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private Set<Usage> usages = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private Set<BoardImage> boardImages = new HashSet<>();

    private void setMember(final Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    protected Board() {}

    public static Board createBoard(
            final String title,
            final String content,
            final Member member,
            final Set<BoardImage> boardImages) {

        final Board board = new Board();
        board.title = title;
        board.content = content;
        board.views = 0;
        board.setMember(member);
        board.boardImages = boardImages;
        board.create();

        return board;
    }
}
