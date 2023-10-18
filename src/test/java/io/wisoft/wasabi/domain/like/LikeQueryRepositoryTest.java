package io.wisoft.wasabi.domain.like;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.like.anonymous.AnonymousLike;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.setting.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(QueryDslTestConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LikeQueryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LikeQueryRepository likeQueryRepository;

    private void init(final Member member,
                      final Board board) {
        em.persist(member);
        em.persist(board);
    }

    @Nested
    @DisplayName("게시글 좋아요 개수 조회")
    class ReadLikeCount {

        @DisplayName("게시글 좋아요 개수 조회 시 비회원, 회원 개수의 총 개수를 조회한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_like_count(final Member member,
                             final Board board,
                             final Long sessionId) {

            // given
            init(member, board);

            final Like like = new Like(member, board);
            em.persist(like);

            final AnonymousLike anonymousLike = new AnonymousLike(sessionId, board);
            em.persist(anonymousLike);

            // when
            final var result = likeQueryRepository.countByBoardId(board.getId());

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result).isEqualTo(2L);
            });
        }
    }

}