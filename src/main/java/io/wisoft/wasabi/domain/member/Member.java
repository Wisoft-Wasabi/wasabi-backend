package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.basetime.BaseTimeEntity;
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
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "reference_url")
    private String referenceUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private Part part;

    @Column
    private String organization;

    @Column
    private String motto;

    @OneToMany(mappedBy = "member")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Board> boards = new HashSet<>();

    public Member(final String email,
                  final String password,
                  final String name,
                  final String phoneNumber,
                  final boolean activation,
                  final Role role,
                  final String referenceUrl,
                  final Part part,
                  final String organization,
                  final String motto) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.activation = activation;
        this.role = role;
        this.referenceUrl = referenceUrl;
        this.part = part;
        this.organization = organization;
        this.motto = motto;
    }

    /* 비즈니스 로직 */
    public void update(final String name,
                       final String phoneNumber,
                       final String referenceUrl,
                       final Part part,
                       final String organization,
                       final String motto) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.referenceUrl = referenceUrl;
        this.part = part;
        this.organization = organization;
        this.motto = motto;
    }

    protected Member() {
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

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReferenceUrl() { return referenceUrl;}

    public Part getPart() {
        return part;
    }

    public String getOrganization() {
        return organization;
    }

    public String getMotto() {
        return motto;
    }
}
