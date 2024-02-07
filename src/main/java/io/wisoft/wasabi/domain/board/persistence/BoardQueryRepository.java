package io.wisoft.wasabi.domain.board.persistence;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wisoft.wasabi.domain.comment.QComment;
import io.wisoft.wasabi.domain.board.web.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.web.dto.SortBoardResponse;
import io.wisoft.wasabi.domain.like.persistence.QAnonymousLike;
import io.wisoft.wasabi.domain.like.persistence.QLike;
import io.wisoft.wasabi.domain.member.persistence.QMember;
import io.wisoft.wasabi.domain.tag.persistence.QTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBoard board = QBoard.board;
    private final QMember member = QMember.member;
    private final QLike like = QLike.like;
    private final QTag tag = QTag.tag;
    private final QComment comment = QComment.comment;
    private final QAnonymousLike anonymousLike = QAnonymousLike.anonymousLike;

    public BoardQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<SortBoardResponse> boardList(final Pageable pageable,
                                              final BoardSortType sortType,
                                              final String keyword) {

        final ConstructorExpression<SortBoardResponse> boardResponse =
                Projections.constructor(
                        SortBoardResponse.class,
                        board.id,
                        board.title,
                        board.member.name,
                        board.createdAt,
                        like.count().add(anonymousLike.count()),
                        board.views
                );

        final List<SortBoardResponse> result = getQueryByTagKeyword(keyword, getJpaQuery(boardResponse))
                .groupBy(board.id)
                .orderBy(ordering(sortType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    public ReadBoardResponse readBoard(final Long boardId, final Long accessId, final boolean isAuthenticated) {

        final ConstructorExpression<ReadBoardResponse.Writer> writer =
                Projections.constructor(
                        ReadBoardResponse.Writer.class,
                        member.email,
                        member.name,
                        member.referenceUrl,
                        member.part,
                        member.organization,
                        member.motto
                );

        final ConstructorExpression<ReadBoardResponse> readBoardResponse =
                Projections.constructor(
                        ReadBoardResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        writer,
                        board.createdAt,
                        like.count().add(anonymousLike.count()),
                        board.views,
                        isLike(boardId, accessId, isAuthenticated),
                        tag.name
                );

        final ReadBoardResponse response = getJpaQuery(readBoardResponse)
                    .leftJoin(tag).on(board.tag.eq(tag))
                    .where(board.id.eq(boardId))
                    .fetchFirst();

        return response.addComments(getComments(boardId));
    }

    private List<ReadBoardResponse.Comment> getComments(final Long boardId) {

        return getJpaQuery(Projections.constructor(
                ReadBoardResponse.Comment.class,
                comment.id,
                comment.content,
                comment.member.id,
                comment.member.name,
                comment.member.id.eq(board.member.id),
                comment.createdAt
        ))
                .leftJoin(comment).on(comment.board.eq(board))
                .where(board.id.eq(boardId))
                .fetch();
    }

    private BooleanExpression isLike(final Long boardId, final Long accessId, final boolean isAuthenticated) {
        return isAuthenticated
                ? JPAExpressions
                .selectFrom(like)
                .where(like.member.id.eq(accessId).and(like.board.id.eq(boardId)))
                .exists()
                : JPAExpressions
                .selectFrom(anonymousLike)
                .where(anonymousLike.sessionId.eq(accessId).and(anonymousLike.board.id.eq(boardId)))
                .exists();
    }

    private JPAQuery<SortBoardResponse> getQueryByTagKeyword(final String keyword,
                                                             final JPAQuery<SortBoardResponse> jpaQuery) {
        if (StringUtils.hasText(keyword)) {
            return jpaQuery
                    .leftJoin(tag)
                    .on(tag.eq(board.tag))
                    .where(tag.name.contains(keyword));
        }

        return jpaQuery;
    }

    private <T> JPAQuery<T> getJpaQuery(final ConstructorExpression<T> constructorExpression) {

        return jpaQueryFactory
                .query()
                .select(constructorExpression)
                .from(board)
                .join(member).on(board.member.eq(member))
                .leftJoin(like).on(like.board.eq(board))
                .leftJoin(anonymousLike).on(anonymousLike.board.eq(board));
    }

    private OrderSpecifier ordering(final BoardSortType sortType) {

        return switch (sortType) {
            case VIEWS -> board.views.desc();
            case LATEST -> board.createdAt.desc();
            case LIKES -> like.count().desc();
            default -> board.createdAt.desc();
        };
    }

}
