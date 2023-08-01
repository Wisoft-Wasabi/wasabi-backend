package io.wisoft.wasabi.domain.like;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.domain.member.Role;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TestEntityManager em;

    private Member member;

    private Board board;

    @BeforeEach
    void init() {
        // Member 초기화
        member = new Member(
                "게시글작성성공@gmail.com",
                "test1234",
                "test1234",
                "01000000000",
                false,
                Role.GENERAL,
                "www.naver.com",
                Part.BACKEND,
                "wisoft",
                "공부는 동엽이처럼");

        System.out.println("여기서 null"+member.getCreatedAt());
        em.persist(member);

        // Board 초기화
        board = Board.createBoard(
                "title",
                "content",
                member
        );
        em.persist(board);
    }

    private Like init(final Member member) {
        em.persist(member);

        final Board board = Board.createBoard(
                "title",
                "content",
                member
        );
        em.persist(board);

        return new Like(member, board);
    }

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @Test
        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        void register_like() throws Exception {

            //given
            final Like like = new Like(member, board);

            //when
            final Like savedLike = likeRepository.save(like);

            //then
            assertNotNull(savedLike);
            assertThat(savedLike.getId()).isEqualTo(like.getId());
            assertThat(savedLike.getBoard()).isEqualTo(board);
        }

        @Test
        @DisplayName("존재하지 않는 데이터 요청 시 등록되지 않는다.")
        void register_like_fail() throws Exception {

            //given

            //when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId());

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
        @Customization(NotSaveMemberCustomization.class)
        void cancel_like(
                final Member member
        ) {

            // given
            final Like like = init(member);
            likeRepository.save(like);

            // when
            like.delete();
            final int result = likeRepository.deleteByMemberIdAndBoardId(
                    like.getMember().getId(),
                    like.getBoard().getId()
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isEqualTo(1);
                softAssertions.assertThat(like.getMember().getLikes().contains(like)).isEqualTo(false);
                softAssertions.assertThat(like.getBoard().getLikes().contains(like)).isEqualTo(false);
            });

        }

        @Test
        @DisplayName("존재하지 않는 데이터 삭제시 아무것도 삭제되지 않는다.")
        void cancel_like_fail() {

            // given

            // when
            final int result = likeRepository.deleteByMemberIdAndBoardId(10L, 10L);

            // then
            assertThat(result).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("좋아요 조회")
    class FindLike {

        @DisplayName("Member Id와 Board Id 조회 시 정상적으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void find_like_by_member_id_and_board_id(
                final Member member
        ) {

            // given
            final Like like = init(member);
            likeRepository.save(like);

            // when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(
                    like.getMember().getId(),
                    like.getBoard().getId()
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result).isNotEmpty();
                softly.assertThat(result.get().getId()).isEqualTo(like.getId());
            });
        }

        @Test
        @DisplayName("Member Id와 Board Id 조회 시 데이터가 없다면 빈값이 조회된다.")
        void find_like_by_member_id_and_board_id_fail() {

            // given

            // when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId());

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {

        @Test
        @DisplayName("BoardId 조회 시 정상적으로 좋아요수가 조회되어야 한다.")
        void count_like_by_board_id() throws Exception {

            //given
            final Like like = new Like(member, board);
            likeRepository.save(like);

            //when
            int result = likeRepository.countByBoardId(board.getId());

            //then
            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("BoardId 조회 시 데이터가 없다면 빈값이 조회되어야 한다.")
        void count_like_by_board_id_fail() throws Exception {

            //given

            //when
            int result = likeRepository.countByBoardId(board.getId());

            //then
            assertThat(result).isZero();
        }
    }
}