package io.wisoft.wasabi.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT board FROM Board board " +
            "LEFT JOIN FETCH board.likes " +
            "LEFT JOIN FETCH board.anonymousLikes annonyLikes " +
            "JOIN FETCH board.member " +
            "WHERE board.member.id = :memberId " +
            "ORDER BY board.createdAt DESC")
    Slice<Board> findAllMyBoards(@Param("memberId") final Long memberId, final Pageable pageable);

    // 내가 좋아요 한 게시글 목록 조회 - 기본값(최신순)
    @Query("SELECT board FROM Board board" +
            " JOIN FETCH board.likes likes" +
            " LEFT JOIN FETCH board.anonymousLikes annonyLikes" +
            " JOIN FETCH board.member member" +
            " WHERE likes.member.id = :id" +
            " ORDER BY board.createdAt DESC")
    Slice<Board> findAllMyLikeBoards(@Param("id") final Long memberId, final Pageable pageable);

    @Query("SELECT EXISTS(SELECT board FROM Board board WHERE board.id = :id)")
    boolean existsById(@Param("id") final Long id);

    /**
     * Board와 관련된 좋아요는 @Formula로 직접 조회하기 때문에 board.like를 조인할 필요가 없어짐
     */
    @Query("SELECT board FROM Board board " +
            "JOIN FETCH board.member LEFT JOIN FETCH board.tag")
    Optional<Board> findBoardById(final Long boardId);
}
