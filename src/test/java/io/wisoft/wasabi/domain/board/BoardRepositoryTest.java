package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.like.LikeMapper;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Spy
    private LikeMapper likeMapper;

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

        private final Pageable pageable = PageRequest.of(0, 2);

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

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회시, 조회수 많은 순으로 정렬 후 조회에 성공한다.")
        void read_boards_order_by_views(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            final Board viewTarget= savedBoards.get(1);
            viewTarget.increaseView();

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByViewsDesc(pageable);

            //then
            final Board mostViewedBoard = sortedBoards.getContent().get(0);
            Assertions.assertThat(viewTarget).isEqualTo(mostViewedBoard);
            Assertions.assertThat(viewTarget.getId()).isEqualTo(mostViewedBoard.getId());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회시, 최신 순으로 정렬 후 조회에 성공한다.")
        void read_boards_order_by_createAt(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

            //then
            final Board mostRecentBoard = sortedBoards.getContent().get(0);
            Assertions.assertThat(savedBoards.get(savedBoards.size() - 1)).isEqualTo(mostRecentBoard);
            Assertions.assertThat(savedBoards.get(savedBoards.size() - 1).getId()).isEqualTo(mostRecentBoard.getId());

        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회시, 좋아요 많은 순 정렬 후 조회에 성공한다.")
        void read_boards_order_by_likes(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            final Board likeTarget = savedBoards.get(0);
            final Board secondLikeTarget = savedBoards.get(1);
            likeRepository.save(likeMapper.registerLikeRequestToEntity(member, likeTarget));

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByLikesDesc(pageable);

            //then
            final Board mostLikedBoard = sortedBoards.getContent().get(0);
            final Board secondLikedBoard = sortedBoards.getContent().get(1);

            Assertions.assertThat(likeTarget).isEqualTo(mostLikedBoard);
            Assertions.assertThat(likeTarget.getId()).isEqualTo(mostLikedBoard.getId());

            Assertions.assertThat(secondLikeTarget).isEqualTo(secondLikedBoard);
            Assertions.assertThat(secondLikeTarget.getId()).isEqualTo(secondLikedBoard.getId());

        }

    }

    private List<Board> saveBoards(final Member member) {
        return boardRepository.saveAll(
                IntStream.range(0, 3)
                        .mapToObj(i -> new Board("title" + i, "content", member))
                        .toList()
        );
    }
}