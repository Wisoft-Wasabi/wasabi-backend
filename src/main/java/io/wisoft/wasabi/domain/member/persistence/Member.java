package io.wisoft.wasabi.domain.member.persistence;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.persistence.Like;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private boolean activation;

    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Board> boards = new HashSet<>();

    public static Member createMember(
            final String email,
            final String password,
            final String name,
            final String phoneNumber,
            final LocalDateTime createAt,
            final Boolean activation,
            final Role role
    ) {
        final Member member = new Member();
        member.email = email;
        member.password = password;
        member.name = name;
        member.phoneNumber = phoneNumber;
        member.createAt = createAt;
        member.activation = activation;
        member.role = role;

        return member;
    }

    public Member() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public Long getId() {
        return id;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Board> getBoards() {
        return boards;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
