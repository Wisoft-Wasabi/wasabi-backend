package io.wisoft.wasabi.domain.board;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.wisoft.wasabi.domain.board.dto.SortBoardResponse;
import io.wisoft.wasabi.domain.like.QLike;
import io.wisoft.wasabi.domain.member.QMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBoard board = QBoard.board;
    private final QMember member = QMember.member;
    private final QLike like = QLike.like;

    public BoardQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<SortBoardResponse> boardList(final Pageable pageable,
                                              final BoardSortType sortType) {

        final ConstructorExpression<SortBoardResponse> boardResponse =
                Projections.constructor(
                        SortBoardResponse.class,
                        board.id,
                        board.title,
                        board.member.name,
                        board.createdAt,
                        like.count(),
                        board.views
                );

        final List<SortBoardResponse> result = jpaQueryFactory
                .query()
                .select(boardResponse)
                .from(board)
                .join(member)
                .on(board.member.eq(member))
                .leftJoin(like)
                .on(like.board.eq(board))
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

    private OrderSpecifier ordering(final BoardSortType sortType) {

        return switch (sortType) {

            case VIEWS -> board.views.desc();
            case LATEST -> board.createdAt.desc();
            case LIKES -> like.count().desc();
            default -> board.createdAt.desc();
        };
    }
}
