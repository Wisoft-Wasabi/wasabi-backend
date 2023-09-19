package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveTagCustomization;
import io.wisoft.wasabi.domain.board.dto.MyLikeBoardsResponse;
import io.wisoft.wasabi.domain.board.dto.SortBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.like.LikeMapper;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.tag.Tag;
import io.wisoft.wasabi.domain.tag.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Spy
    private BoardMapper boardMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private BoardQueryRepository boardQueryRepository;

    @Mock
    private LikeRepository likeRepository;

    @Spy
    private LikeMapper likeMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 태그가 이미 존재한다면 조회한 태그를 이용해 게시글을 저장한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveTagCustomization.class)
        void write_board_with_tag(final Member member, final Tag tag) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final var request = new WriteBoardRequest(
                    "title",
                    "content",
                    "tag",
                    new String[]{"imageUrls"});

            final var board = boardMapper.writeBoardRequestToEntity(request, member);

            given(tagRepository.save(any())).willReturn(tag);
            given(boardRepository.save(any())).willReturn(board);


            // when
            final var response = boardServiceImpl.writeBoard(request, 1L);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.title()).isEqualTo("title");
                softAssertions.assertThat(tag.getName()).isEqualTo("tag");
            });

        }

        @DisplayName("요청시 저장된 태그가 없다면 태그가 저장된 후 게시글을 저장한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveTagCustomization.class)
        void write_board_with_none_tag(final Member member, final Tag tag) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(tagRepository.save(any())).willReturn(tag);

            final var request = new WriteBoardRequest(
                    "title",
                    "content",
                    "tag",
                    new String[]{"imageUrls"});

            final var board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.save(any())).willReturn(board);

            // when
            final var response = boardServiceImpl.writeBoard(request, 1L);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.title()).isEqualTo("title");
                softAssertions.assertThat(tag.getName()).isEqualTo("tag");
            });
        }

        @DisplayName("요청시 태그가 유효하지 않은 값이면 null로 저장된다.")
        @ParameterizedTest
        @AutoSource
        void write_board_with_null(final Member member) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final var request = new WriteBoardRequest(
                    "title",
                    "content",
                    null,
                    new String[]{"imageUrls"});


            final var board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.save(any())).willReturn(board);

            // when
            final var response = boardServiceImpl.writeBoard(request, 1L);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.title()).isEqualTo("title");
                softAssertions.assertThat(board.getTag()).isEqualTo(null);
            });
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        final Pageable pageable = PageRequest.of(0, 2);

        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final Member member) {

            //given
            final var request = new WriteBoardRequest(
                    "title",
                    "content",
                    "tag",
                    new String[]{"imageUrls"});

            final var board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            //when
            final var response = boardServiceImpl.readBoard(board.getId(), member.getId());

            //then
            assertThat(response.views()).isEqualTo(1L);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회수 순 정렬 조회시, 가장 조회수가 많은 게시물이 첫 번째로 보인다.")
        void read_boards_order_by_views(
                final Board board1,
                final Board board2) {

            //given
            board2.increaseView();

            final var boards = new SliceImpl<>(List.of(board2, board1));
            final var boardList = boards.map(board -> new SortBoardResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getMember().getName(),
                    board.getCreatedAt(),
                    board.getLikes().size(),
                    board.getViews()
            ));

            given(boardQueryRepository.boardList(pageable, BoardSortType.VIEWS, "tag")).willReturn(boardList);

            //when
            final var sortedBoards = boardServiceImpl.getBoardList("views", pageable, "tag");

            //then
            final var mostViewedBoard = (SortBoardResponse) sortedBoards.getContent().get(0);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(mostViewedBoard.id()).isEqualTo(board2.getId());
                softAssertions.assertThat(mostViewedBoard.title()).isEqualTo(board2.getTitle());
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 최신순 정렬 조회시, 가장 최근 게시물이 첫 번째로 보인다.")
        @Customization(NotSaveBoardCustomization.class)
        void read_boards_order_by_created_at(
                final Board board1,
                final Board board2,
                final Board board3) {

            //given
            final var boards = new SliceImpl<>(List.of(board3, board2, board1));
            final var boardList = boards.map(board -> new SortBoardResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getMember().getName(),
                    board.getCreatedAt(),
                    board.getLikes().size(),
                    board.getViews()
            ));

            given(boardQueryRepository.boardList(pageable, BoardSortType.LATEST, "tag")).willReturn(boardList);

            //when
            final var sortedBoards = boardServiceImpl.getBoardList("latest", pageable, "tag");

            //then
            final var latestBoard = (SortBoardResponse) sortedBoards.getContent().get(0);
            assertThat(latestBoard.title()).isEqualTo(board1.getTitle());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 좋아요 순 정렬 조회시, 가장 좋아요가 많은 게시물이 첫 번째로 보인다.")
        void read_boards_order_by_likes(
                final Board board1,
                final Board board2,
                final Member member) {

            //given
            given(memberRepository.save(member)).willReturn(member);
            memberRepository.save(member);

            final var like = likeMapper.registerLikeRequestToEntity(member, board2);
            given(likeRepository.save(like)).willReturn(like);
            likeRepository.save(like);

            final var boards = new SliceImpl<>(List.of(board2, board1));
            final var boardList = boards.map(board -> new SortBoardResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getMember().getName(),
                    board.getCreatedAt(),
                    board.getLikes().size(),
                    board.getViews()
            ));

            given(boardQueryRepository.boardList(pageable, BoardSortType.LIKES, "tag")).willReturn(boardList);

            //when
            final var sortedBoards = boardServiceImpl.getBoardList("likes", pageable, "tag");

            //then
            final var mostLikedBoard = (SortBoardResponse) sortedBoards.getContent().get(0);
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(mostLikedBoard.id()).isEqualTo(board2.getId());
                softAssertions.assertThat(mostLikedBoard.title()).isEqualTo(board2.getTitle());
            });
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시글 목록이 최신순으로 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void read_my_Boards(
                final Long memberId,
                final List<Board> boardList
        ) {

            // given
            final var boards = new SliceImpl<>(boardList);
            given(boardRepository.findAllMyBoards(any(), any())).willReturn(boards);

            // when
            final var myBoards = boardServiceImpl.getMyBoards(memberId, pageable);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(myBoards).isNotEmpty();
                softAssertions.assertThat(myBoards.getSize()).isEqualTo(3);
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글 목록이 최신순으로 조회된다.")
        @Customization(NotSaveBoardCustomization.class)
        void read_my_like_boards(final Long memberId,
                                 final Board board1,
                                 final Board board2,
                                 final Board board3) {

            // given
            final var boards = new SliceImpl<>(List.of(board3, board2, board1));
            given(boardRepository.findAllMyLikeBoards(any(), any())).willReturn(boards);

            // when
            final var pageable = PageRequest.of(0, 3);
            final Slice<MyLikeBoardsResponse> myLikeBoards = boardServiceImpl.getMyLikeBoards(memberId, pageable);

            // then
            assertSoftly(softAssertions -> {
                final var response1 = myLikeBoards.getContent().get(0);
                final var response2 = myLikeBoards.getContent().get(1);
                final var response3 = myLikeBoards.getContent().get(2);

                softAssertions.assertThat(myLikeBoards.getSize()).isEqualTo(3);
                softAssertions.assertThat(response1.createdAt()).isAfter(response2.createdAt());
                softAssertions.assertThat(response2.createdAt()).isAfter(response3.createdAt());
            });
        }
    }
}

