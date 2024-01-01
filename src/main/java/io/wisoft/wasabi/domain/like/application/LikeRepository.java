package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.like.persistence.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT like FROM Like like WHERE like.member.id = :memberId AND like.board.id = :boardId")
    Optional<Like> findByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Like like WHERE like.member.id = :memberId AND like.board.id = :boardId")
    int deleteByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);

    @Query("SELECT count(like) FROM Like like WHERE like.board.id = :boardId")
    int countByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT EXISTS(SELECT like FROM Like like WHERE like.member.id = :memberId AND like.board.id = :boardId)")
    boolean existsByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);
}
