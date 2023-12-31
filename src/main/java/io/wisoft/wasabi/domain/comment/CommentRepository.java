package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
