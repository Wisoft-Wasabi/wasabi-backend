package io.wisoft.wasabi.domain.like.application;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.like.persistence.AnonymousLike;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.constraints.Min;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnonymousLikeRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AnonymousLikeRepository anonymousLikeRepository;

    private AnonymousLike init(final Member member,
                               final Board board,
                               final Long sessionId) {

        em.persist(member);
        em.persist(board);

        return new AnonymousLike(sessionId, board);
    }

    @Nested
    @DisplayName("비회원 좋아요 등록")
    class RegisterAnonymousLike {

        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void register_anonymous_like(final Member member,
                                     final Board board,
                                     @Min(1) final Long sessionId) {

            // given
            final AnonymousLike anonymousLike = init(member, board, sessionId);

            // when
            final var result = anonymousLikeRepository.save(anonymousLike);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.getId()).isNotNull();
            });
        }
    }

    @Nested
    @DisplayName("비회원 좋아요 조회")
    class FindAnonymousLike {

        @DisplayName("Session Id와 Board Id로 조회 시 정상적으로 조회되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void find_anonymous_like_by_session_id_and_board_id(final Member member,
                                                            final Board board,
                                                            @Min(1) final Long sessionId) {

            // given
            final AnonymousLike anonymousLike = init(member, board, sessionId);

            anonymousLikeRepository.save(anonymousLike);

            // when
            final var result = anonymousLikeRepository.findBySessionIdAndBoardId(sessionId, board.getId())
                .orElseThrow(LikeExceptionExecutor::LikeNotFound);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.getId()).isEqualTo(anonymousLike.getId());
            });
        }
    }

    @Nested
    @DisplayName("비회원 좋아요 취소")
    class CancelAnonymousLike {

        @DisplayName("요청 시 정상적으로 삭제되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void cancel_anonymous_like(final Member member,
                                   final Board board,
                                   @Min(1) final Long sessionId) {

            // given
            final AnonymousLike anonymousLike = init(member, board, sessionId);

            anonymousLikeRepository.save(anonymousLike);

            // when
            anonymousLikeRepository.delete(anonymousLike);

            // then
            assertThatThrownBy(() ->
                anonymousLikeRepository.findBySessionIdAndBoardId(sessionId, board.getId())
                    .orElseThrow(LikeExceptionExecutor::LikeNotFound)
            );
        }
    }

    @Nested
    @DisplayName("비회원 좋아요 유무 조회")
    class ExistsAnonymousLike {

        @DisplayName("Session Id와 Board Id로 요청 시 정상적으로 참의 값을 반환한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void exists_anonymous_like_by_session_id_and_board_id(final Member member,
                                                              final Board board,
                                                              final Long sessionId) {

            // given
            final AnonymousLike anonymousLike = init(member, board, sessionId);

            anonymousLikeRepository.save(anonymousLike);

            // when
            final var result = anonymousLikeRepository.existsBySessionIdAndBoardId(sessionId, board.getId());

            // then
            assertThat(result).isTrue();
        }
    }
}