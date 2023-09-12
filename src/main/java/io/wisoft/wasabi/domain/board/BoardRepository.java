package io.wisoft.wasabi.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT board FROM Board board " +
            "LEFT JOIN FETCH board.likes " +
            "JOIN FETCH board.member " +
            "WHERE board.member.id = :memberId " +
            "ORDER BY board.createdAt DESC")
    Slice<Board> findAllMyBoards(@Param("memberId") final Long memberId, final Pageable pageable);

    // 내가 좋아요 한 게시글 목록 조회 - 기본값(최신순)
    @Query("SELECT board FROM Board board" +
            " JOIN board.likes likes" +
            " JOIN FETCH board.member member" +
            " WHERE likes.member.id = :id" +
            " ORDER BY board.createdAt DESC")
    Slice<Board> findAllMyLikeBoards(@Param("id") final Long memberId, final Pageable pageable);
}
