package io.wisoft.wasabi.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 최신순
    List<Board> findAllByOrderByCreatedAtDesc();

    // 조회수
    List<Board> findAllByOrderByViewsDesc();

    // 좋아요 순
    List<Board> findAllByOrderByLikesDesc();
}
