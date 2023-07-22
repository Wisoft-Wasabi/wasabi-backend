package io.wisoft.wasabi.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Like like WHERE like.member.id= :memberId AND like.board.id= :boardId")
    int deleteByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);

}
