package io.wisoft.wasabi.domain.like;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("좋아요 취소")
    class CancelLike {

        @DisplayName("요청 시 정상적으로 삭제되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void cancel_like(final Member member,
                         final Board board) {

            // given
            final Like like = init(member, board);

            likeRepository.save(like);

            // when
            final int result = likeRepository.deleteByMemberIdAndBoardId(
                    member.getId(),
                    board.getId()
            );

            // then
            assertThat(result).isOne();
        }

        @DisplayName("존재하지 않는 데이터 삭제시 아무것도 삭제되지 않는다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like_fail(final Long memberId,
                              final Long boardId) {

            // given

            // when
            final int result = likeRepository.deleteByMemberIdAndBoardId(memberId, boardId);

            // then
            assertThat(result).isZero();
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
            final var result = likeRepository.findByMemberIdAndBoardId(
                    member.getId(),
                    board.getId()
            ).orElseThrow();

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
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {

        @DisplayName("BoardId 조회 시 정상적으로 좋아요수가 조회되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void count_like_by_board_id(final Member member,
                                    final Board board) {

            //given
            final Like like = init(member, board);
            likeRepository.save(like);

            //when
            int result = likeRepository.countByBoardId(board.getId());

            //then
            assertThat(result).isOne();
        }

        @DisplayName("BoardId 조회 시 데이터가 없다면 빈값이 조회되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void count_like_by_board_id_fail(final Long boardId) {

            //given

            //when
            int result = likeRepository.countByBoardId(boardId);

            //then
            assertThat(result).isZero();
        }
    }
}