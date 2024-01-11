package io.wisoft.wasabi.domain.comment;

import io.wisoft.wasabi.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findByBoard_Id(Long boardId);

}
