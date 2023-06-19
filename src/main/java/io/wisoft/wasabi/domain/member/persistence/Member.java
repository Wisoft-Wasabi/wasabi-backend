package io.wisoft.wasabi.domain.member.persistence;

import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.persistence.Like;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String photoPath;
    private String oAuthId;

    @OneToMany(mappedBy = "member")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private Set<Board> boards = new HashSet<>();

    public static Member registerMember(
            final String email,
            final String name,
            final String photoPath,
            final String oAuthId) {

        final Member member = new Member();
        member.email = email;
        member.name = name;
        member.photoPath = photoPath;
        member.oAuthId = oAuthId;

        return member;
    }
}
