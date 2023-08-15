package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.customization.composite.BoardCompositeCustomizer;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.like.LikeMapper;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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
        @Customization(BoardCompositeCustomizer.class)
        void write_board(final Member member,
                         final Board board) throws Exception {

            // given
            memberRepository.save(member);

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
        private final Pageable pageable = PageRequest.of(0, 12);

        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_board_success(final Member member,
                                final Board board) throws Exception {

            //given
            memberRepository.save(member);

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
        @Customization({NotSaveMemberCustomization.class, NotSaveBoardCustomization.class})
        void read_boards_order_by_views(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            final Board viewTarget = savedBoards.get(1);
            viewTarget.increaseView();

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByViewsDesc(pageable);

            //then
            final Board mostViewedBoard = sortedBoards.getContent().get(0);

            Assertions.assertThat(viewTarget.getViews()).isEqualTo(mostViewedBoard.getViews());

        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회시, 최신 순으로 정렬 후 조회에 성공한다.")
        @Customization(NotSaveMemberCustomization.class)
        void read_boards_order_by_create_at(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

            //then
            final Board savedBoard = savedBoards.get(savedBoards.size() - 1);
            final Board mostRecentBoard = sortedBoards.getContent().get(0);

            Assertions.assertThat(savedBoard)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(mostRecentBoard);
        }


        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회시, 좋아요 많은 순 정렬 후 조회에 성공한다.")
        @Customization({NotSaveMemberCustomization.class, NotSaveBoardCustomization.class})
        void read_boards_order_by_likes(final Member member) throws Exception {

            //given
            memberRepository.save(member);
            final List<Board> savedBoards = saveBoards(member);

            final Board likeTarget = savedBoards.get(0);

            likeRepository.save(likeMapper.registerLikeRequestToEntity(member, likeTarget));

            //when
            final Slice<Board> sortedBoards = boardRepository.findAllByOrderByLikesDesc(pageable);

            //then
            final Board mostLikedBoard = sortedBoards.getContent().get(0);

            Assertions.assertThat(likeTarget.getLikes().size()).isEqualTo(mostLikedBoard.getLikes().size());
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시글들만 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_my_boards(final Member member,
                            final List<Board> boards) {

            // given
            memberRepository.save(member);

            boardRepository.saveAll(boards);

            final List<Like> likes = List.of(
                    new Like(member, boards.get(0)),
                    new Like(member, boards.get(1))
            );
            likeRepository.saveAll(likes);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var myBoards = boardRepository.findAllMyBoards(member.getId(), pageable);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(myBoards.getContent()).isNotEmpty();
                softly.assertThat(myBoards.getContent().size()).isEqualTo(3);
            });
        }

        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글들만 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(BoardCompositeCustomizer.class)
        void read_my_like_boards(final Member member,
                                 final Board board1,
                                 final Board board2) {

            // given
            memberRepository.save(member);

            boardRepository.save(board1);
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

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성하지 않은 게시글 목록은 조회되지 않는다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void read_not_my_boards(
                final List<Member> members
        ) {

            // given
            memberRepository.saveAll(members);

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

            final List<Like> likes = List.of(
                    new Like(members.get(0), boards.get(0)),
                    new Like(members.get(1), boards.get(1))
            );
            likeRepository.saveAll(likes);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var myBoards = boardRepository.findAllMyBoards(members.get(0).getId(), pageable);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(myBoards.getContent()).isNotEmpty();
                softly.assertThat(myBoards.getContent().size()).isEqualTo(1);
            });
        }

        private List<Board> saveBoards(final Member member) {
            return boardRepository.saveAll(
                    IntStream.range(0, 10)
                            .mapToObj(i -> new Board("title" + i, "content", member))
                            .toList()
            );
        }

    }
}


