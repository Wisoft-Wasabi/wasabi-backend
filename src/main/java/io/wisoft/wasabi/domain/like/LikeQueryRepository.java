package io.wisoft.wasabi.domain.like;

import com.querydsl.core.types.SubQueryExpressionImpl;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wisoft.wasabi.domain.board.QBoard;
import io.wisoft.wasabi.domain.like.anonymous.QAnonymousLike;
import org.springframework.stereotype.Repository;

@Repository
public class LikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QLike like = QLike.like;
    private final QAnonymousLike anonymousLike = QAnonymousLike.anonymousLike;
    private final QBoard board = QBoard.board;

    public LikeQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Long countByBoardId(final Long boardId) {

        return
            jpaQueryFactory
                .select(like.count().add(anonymousLike.count()))
                .from(board)
                .leftJoin(like).on(like.board.eq(board))
                .leftJoin(anonymousLike).on(anonymousLike.board.eq(board))
                .where(board.id.eq(boardId))
                .fetchFirst();
    }

}
