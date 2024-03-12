package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.like.persistence.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT like FROM Like like WHERE like.member.id = :memberId AND like.board.id = :boardId")
    Optional<Like> findByMemberIdAndBoardId(@Param("memberId") final Long memberId, @Param("boardId") final Long boardId);

    @Query("SELECT EXISTS(SELECT like FROM Like like WHERE like.member.id = :memberId AND like.board.id = :boardId)")
    boolean existsByMemberIdAndBoardId(@Param("memberId") final Long memberId, @Param("boardId") final Long boardId);
}
