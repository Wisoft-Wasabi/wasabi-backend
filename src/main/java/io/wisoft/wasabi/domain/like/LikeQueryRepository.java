package io.wisoft.wasabi.domain.like;

import com.querydsl.core.types.SubQueryExpressionImpl;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wisoft.wasabi.domain.like.anonymous.QAnonymousLike;
import org.springframework.stereotype.Repository;

@Repository
public class LikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QLike like = QLike.like;
    private final QAnonymousLike anonymousLike = QAnonymousLike.anonymousLike;

    public LikeQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Long countByBoardId(final Long boardId) {

        final Long likeCount =
            jpaQueryFactory.select(like.count())
                .from(like)
                .where(like.board.id.eq(boardId))
                .fetchFirst();

        final Long anonymousLikeCount =
            jpaQueryFactory.select(anonymousLike.count())
                .from(anonymousLike)
                .where(anonymousLike.board.id.eq(boardId))
                .fetchFirst();

        return likeCount + anonymousLikeCount;
    }

}
