package io.wisoft.wasabi.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 기본값
    @Query("SELECT board FROM Board board")
    Slice<Board> findDefaultBoards(final Pageable pageable);
    // 최신순
    @Query("SELECT board FROM Board board ORDER BY board.createdAt DESC")
    Slice<Board> findAllByOrderByCreatedAtDesc(final Pageable pageable);

    // 조회수
    @Query("SELECT board FROM Board board ORDER BY board.views DESC")
    Slice<Board> findAllByOrderByViewsDesc(final Pageable pageable);

    // 좋아요 순
    @Query("SELECT board FROM Board board ORDER BY SIZE(board.likes) DESC")
    Slice<Board> findAllByOrderByLikesDesc(final Pageable pageable);
}