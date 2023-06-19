package io.wisoft.wasabi.domain.board.persistence;

import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.usage.persistence.Usage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private String body;

    private int views;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany(mappedBy = "board")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private Set<Usage> usages = new HashSet<>();

    public void setMember(final Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    public static Board writeBoard(
            final String title,
            final String body,
            final int views,
            final Member member) {

        final Board board = new Board();
        board.title = title;
        board.body = body;
        board.views = views;
        board.setMember(member);

        return board;
    }
}
