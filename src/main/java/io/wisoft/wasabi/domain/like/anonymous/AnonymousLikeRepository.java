package io.wisoft.wasabi.domain.like.anonymous;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnonymousLikeRepository extends CrudRepository<AnonymousLike, Long> {

    @Query("SELECT like FROM AnonymousLike like WHERE like.sessionId = :sessionId AND like.board.id = :boardId")
    Optional<AnonymousLike> findBySessionIdAndBoardId(@Param("sessionId") Long sessionId, @Param("boardId") Long boardId);

    @Query("SELECT EXISTS(SELECT like FROM AnonymousLike like WHERE like.sessionId = :sessionId AND like.board.id = :boardId)")
    boolean existsBySessionIdAndBoardId(@Param("sessionId") Long sessionId, @Param("boardId") Long boardId);
}
