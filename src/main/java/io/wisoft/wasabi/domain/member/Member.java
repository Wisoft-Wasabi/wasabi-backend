package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.global.basetime.BaseTimeEntity;
import io.wisoft.wasabi.global.enumeration.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private boolean activation;

    @Column
    private Role role;

    @OneToMany(mappedBy = "member")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Board> boards = new HashSet<>();

    public Member(final String email,
                  final String password,
                  final String name,
                  final String phoneNumber,
                  final boolean activation,
                  final Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.activation = activation;
        this.role = role;
    }

    protected Member() {
    }

    public static Member createMember(
            final String email,
            final String password,
            final String name,
            final String phoneNumber,
            final boolean activation,
            final Role role) {
        return new Member(
                email,
                password,
                name,
                phoneNumber,
                activation,
                role
        );
    }

    public Long getId() {
        return id;
    }

    public Set<Board> getBoards() {
        return boards;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActivation() {
        return activation;
    }

    public String getPassword() {
        return password;
    }
}
