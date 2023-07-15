package io.wisoft.wasabi.domain.member.persistence;

import io.wisoft.wasabi.domain.auth.dto.request.MemberSignupRequest;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.global.enumeration.Role;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            MemberSignupRequest request
    ) {

        final Member member = new Member();

        // 2. DTO 값 기반으로 member 생성.
        // 3. 생성 중 예외 발생 시 예외 처리.

        member.email = request.email();
        member.password = BCrypt.hashpw(request.password(), BCrypt.gensalt(10));
        member.name = request.name();
        member.phoneNumber = request.phoneNumber();
        member.activation = false;
        member.role=request.role();
        member.createAt = LocalDateTime.now().withNano(0);

        return member;
    }

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public boolean checkPassword(String expectedPassword) {
        return BCrypt.checkpw(expectedPassword, this.password);
    }

}
