package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.basetime.BaseTimeEntity;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.like.anonymous.AnonymousLike;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.tag.Tag;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int views;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "board")
    private Set<Like> likes = new HashSet<>();

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = LAZY)
    private Tag tag;

    @OneToMany(mappedBy = "board")
    private Set<BoardImage> boardImages = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private Set<AnonymousLike> anonymousLikes = new HashSet<>();

    @Formula("(SELECT COUNT(1) FROM likes l WHERE l.board_id = id)")
    private Long likeCount;

    @Formula("(SELECT COUNT(1) FROM anonymous_likes al WHERE al.board_id = id)")
    private Long anonymousLikeCount;

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getAnonymousLikeCount() {
        return anonymousLikeCount;
    }

    private void setMember(final Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    protected Board() {
    }

    public Board(
            final String title,
            final String content,
            final Member member) {
        this.title = title;
        this.content = content;
        this.views = 0;
        setMember(member);
    }

    /* 비즈니스 로직 */
    public void increaseView() {
        this.views++;
    }

    /* getter */
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Member getMember() {
        return member;
    }

    public Tag getTag() {
        return tag;
    }
    public Set<Like> getLikes() {
        return likes;
    }

    public Set<BoardImage> getBoardImages() {
        return boardImages;
    }

    public int getViews() {
        return views;
    }

    void setTag(final Tag tag) {
        this.tag = tag;
        tag.addBoard(this);
    }

    public void addAnonymousLike(final AnonymousLike anonymousLike) {
        this.anonymousLikes.add(anonymousLike);
    }

    public void removeAnonymousLike(final AnonymousLike anonymousLike) {
        this.anonymousLikes.remove(anonymousLike);
    }
}
