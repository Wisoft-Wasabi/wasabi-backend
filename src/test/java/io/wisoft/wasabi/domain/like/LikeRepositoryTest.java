package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.global.enumeration.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
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
        member = Member.createMember(
                "게시글작성성공@gmail.com",
                "test1234",
                "test1234",
                "01000000000",
                false,
                Role.GENERAL);

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
            assertEquals(savedLike.getId(), like.getId());
            assertEquals(savedLike.getBoard(), board);
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @Test
        @DisplayName("요청 시 정상적으로 삭제되어야 한다.")
        void cancel_like() {

            // given
            final Like like = new Like(member, board);
            likeRepository.save(like);

            // when
            final int result = likeRepository.deleteByMemberIdAndBoardId(member.getId(), board.getId());

            // then
            Assertions.assertThat(result).isEqualTo(1);

        }

        @Test
        @DisplayName("존재하지 않는 데이터 삭제시 아무것도 삭제되지 않는다.")
        void cancel_like_fail() {

            // given

            // when
            final int result = likeRepository.deleteByMemberIdAndBoardId(10L, 10L);

            // then
            Assertions.assertThat(result).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("좋아요 조회")
    class FindLike {

        @Test
        @DisplayName("Member Id와 Board Id 조회 시 정상적으로 조회된다.")
        void find_like_by_member_id_and_board_id() {

            // given
            final Like like = new Like(member, board);
            likeRepository.save(like);

            // when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId());

            // then
            Assertions.assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Member Id와 Board Id 조회 시 데이터가 없다면 빈값이 조회된다.")
        void find_like_by_member_id_and_board_id_fail() {

            // given

            // when
            final Optional<Like> result = likeRepository.findByMemberIdAndBoardId(member.getId(), board.getId());

            // then
            Assertions.assertThat(result).isEmpty();
        }

    }
}