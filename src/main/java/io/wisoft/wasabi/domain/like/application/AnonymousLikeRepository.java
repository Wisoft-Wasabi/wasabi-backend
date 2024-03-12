package io.wisoft.wasabi.domain.like.application;

import io.wisoft.wasabi.domain.like.persistence.AnonymousLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnonymousLikeRepository extends CrudRepository<AnonymousLike, Long> {

    @Query("SELECT like FROM AnonymousLike like WHERE like.sessionId = :sessionId AND like.board.id = :boardId")
    Optional<AnonymousLike> findBySessionIdAndBoardId(@Param("sessionId") final Long sessionId, @Param("boardId") final Long boardId);

    @Query("SELECT EXISTS(SELECT like FROM AnonymousLike like WHERE like.sessionId = :sessionId AND like.board.id = :boardId)")
    boolean existsBySessionIdAndBoardId(@Param("sessionId") final Long sessionId, @Param("boardId") final Long boardId);
}
