package io.wisoft.wasabi.domain.like.application;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.like.exception.LikeExceptionExecutor;
import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LikeRepository likeRepository;

    private Like init(final Member member,
                      final Board board) {

        em.persist(member);
        em.persist(board);

        return new Like(member, board);
    }

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void register_like(final Member member,
                           final Board board) {

            //given
            final Like like = init(member, board);

            //when
            final var result = likeRepository.save(like);

            //then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.getId()).isNotNull();
            });
        }

        @DisplayName("존재하지 않는 데이터 요청 시 등록되지 않는다.")
        @ParameterizedTest
        @AutoSource
        void register_like_fail(final Long memberId,
                                final Long boardId) {

            //given

            //when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(memberId, boardId);

            //then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("좋아요 조회")
    class FindLike {

        @DisplayName("Member Id와 Board Id 조회 시 정상적으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void find_like_by_member_id_and_board_id(final Member member,
                                                 final Board board) {

            // given
            final Like like = init(member, board);

            final var expected = likeRepository.save(like);

            // when
            final var result = likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId())
                    .orElseThrow();

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.getId()).isEqualTo(expected.getId());
            });
        }

        @DisplayName("Member Id와 Board Id 조회 시 데이터가 없다면 빈값이 조회된다.")
        @ParameterizedTest
        @AutoSource
        void find_like_by_member_id_and_board_id_fail(final Long memberId,
                                                      final Long boardId) {

            // given

            // when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(memberId, boardId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @DisplayName("요청 시 정상적으로 취소되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void cancel_like(final Member member,
                         final Board board) {

            // given
            final Like like = init(member, board);

            likeRepository.save(like);

            // when
            like.delete();
            likeRepository.deleteById(like.getId());

            // then
            assertThatThrownBy(() ->
                    likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId())
                            .orElseThrow(LikeExceptionExecutor::LikeNotFound));
        }
    }
}