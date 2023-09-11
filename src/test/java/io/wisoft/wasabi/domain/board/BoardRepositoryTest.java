package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BoardRepository boardRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void write_board(final Member member,
                         final Board board) {

            // given
            em.persist(member);

            // when
            final var result = boardRepository.save(board);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getTitle()).isEqualTo(board.getTitle());
                softAssertions.assertThat(result.getContent()).isEqualTo(board.getContent());
            });
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        private final Pageable pageable = PageRequest.of(0, 3);

        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_board_success(final Member member,
                                final Board board) {

            //given
            em.persist(member);

            final var expected = boardRepository.save(board);

            //when
            final var result = boardRepository.findById(board.getId()).get();

            //then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getTitle()).isEqualTo(expected.getTitle());
                softAssertions.assertThat(result.getContent()).isEqualTo(expected.getContent());
            });
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시글들만 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_my_boards(final Member member,
                            final List<Board> boards) {

            // given
            em.persist(member);
            boardRepository.saveAll(boards);
            boards.stream()
                .map(board -> new Like(member, board))
                .forEach(em::persist);

            // when
            final var result = boardRepository.findAllMyBoards(member.getId(), pageable);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent()).isNotEmpty();
                softAssertions.assertThat(result.getContent().size()).isEqualTo(boards.size());

            });
        }

        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글들만 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_my_like_boards(final Member member,
                                 final List<Board> boards) {

            // given
            em.persist(member);
            boardRepository.saveAll(boards);
            boards.stream()
                .map(board -> new Like(member, board))
                .forEach(em::persist);

            final var expected = member.getLikes();

            // when
            final var result = boardRepository.findAllMyLikeBoards(member.getId(), pageable);

            // then
            assertThat(result.getContent().size()).isEqualTo(expected.size());
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성하지 않은 게시글 목록은 조회되지 않는다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void read_not_my_boards(final List<Member> members) {

            // given
            members.forEach(em::persist);

            final List<Board> boards = List.of(
                new Board(
                    "title",
                    "content",
                    members.get(0)
                ),
                new Board(
                    "title",
                    "content",
                    members.get(1)
                )
            );
            boardRepository.saveAll(boards);

            final var expected = members.get(0).getBoards();

            // when
            final var result = boardRepository.findAllMyBoards(members.get(0).getId(), pageable);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent()).isNotEmpty();
                softAssertions.assertThat(result.getContent().size()).isEqualTo(expected.size());
            });
        }

    }
}


