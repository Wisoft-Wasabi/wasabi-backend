package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board(final Member member) throws Exception {

            // given
            memberRepository.save(member);

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board);

            // when
            final Board savedBoard = boardRepository.save(board);

            // then
            final Board findBoard = boardRepository.findById(savedBoard.getId()).get();
            assertEquals("title", findBoard.getTitle());
            assertEquals("content", findBoard.getContent());
        }
    }


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board);

            //when
            final Board findBoard = boardRepository.findById(board.getId()).get();

            //then
            Assertions.assertThat(findBoard.getTitle()).isEqualTo("title");
            Assertions.assertThat(findBoard.getContent()).isEqualTo("content");
        }

        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글들만 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        void read_my_like_boards(final Member member) {

            // given
            memberRepository.save(member);

            final var board1 = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board1);

            final var board2 = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board2);

            final var like1 = new Like(member, board1);
            likeRepository.save(like1);

            final var like2 = new Like(member, board2);
            likeRepository.save(like2);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var myLikeBoards = boardRepository.findAllMyLikeBoards(member.getId(), pageable);

            // then
            assertSoftly(softly -> {
                softly.assertThat(myLikeBoards.getContent().size()).isEqualTo(member.getLikes().size());
                softly.assertThat(myLikeBoards.getContent().get(0)).isEqualTo(board2);
            });
        }
    }
}