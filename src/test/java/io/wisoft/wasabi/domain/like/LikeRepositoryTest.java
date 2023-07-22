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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void init() {
        // Member 초기화
        final Member member = Member.createMember(
                "게시글작성성공@gmail.com",
                "test1234",
                "test1234",
                "01000000000",
                false,
                Role.GENERAL);
        em.persist(member);

        // Board 초기화
        final Board board = Board.createBoard(
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
            final Member member = em.find(Member.class, 1L);

            final Board board = em.find(Board.class, 1L);

            final Like like = Like.createLike(member, board);

            //when
            final Like savedLike = likeRepository.save(like);

            //then
            assertNotNull(savedLike);
            Assertions.assertThat(savedLike.getId()).isEqualTo(like.getId());
            Assertions.assertThat(savedLike.getBoard()).isEqualTo(board);
        }
    }
}