package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.like.Like;
import io.wisoft.wasabi.domain.like.LikeMapper;
import io.wisoft.wasabi.domain.like.LikeRepository;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
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
    private LikeRepository likeRepository;

    @Spy
    private LikeMapper likeMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board(final Member member) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final Board board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.save(any())).willReturn(board);

            // when
            final WriteBoardResponse response = boardServiceImpl.writeBoard(request, 1L);

            // then
            assertEquals("title", response.title());
            assertNotNull(response);
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {
        final Pageable pageable = PageRequest.of(0, 2);

        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final Member member) throws Exception {

            //given
            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final Board board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            //when
            final var response = boardServiceImpl.readBoard(board.getId());

            //then
            Assertions.assertThat(response.views()).isEqualTo(1L);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 조회수 순 정렬 조회시, 가장 조회수가 많은 게시물이 첫 번째로 보인다.")
        void read_boards_order_by_views(
                final Board board1,
                final Board board2) throws Exception {

            //given
            board2.increaseView();

            final Slice<Board> boards = new SliceImpl<>(List.of(board2, board1));
            given(boardRepository.findAllByOrderByViewsDesc(pageable)).willReturn(boards);

            //when
            final var sortedBoards = boardServiceImpl.getSortedBoards("views", pageable);

            //then
            final var mostViewedBoard = sortedBoards.getContent().get(0);
            Assertions.assertThat(sortedBoards.getSize()).isEqualTo(2);
            Assertions.assertThat(mostViewedBoard.title()).isEqualTo(board2.getTitle());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 최신순 정렬 조회시, 가장 최근 게시물이 첫 번째로 보인다.")
        void read_boards_order_by_created_at(
                final Board board1,
                final Board board2,
                final Board board3) throws Exception {

            //given
            final Slice<Board> boards = new SliceImpl<>(List.of(board3, board2, board1));
            given(boardRepository.findAllByOrderByCreatedAtDesc(pageable)).willReturn(boards);

            //when
            final var sortedBoards = boardServiceImpl.getSortedBoards("latest", pageable);

            //then
            final var response1 = sortedBoards.getContent().get(0);
            final var response2 = sortedBoards.getContent().get(1);
            final var response3 = sortedBoards.getContent().get(2);

            Assertions.assertThat(sortedBoards.getSize()).isEqualTo(3);
            Assertions.assertThat(response1.createdAt()).isAfter(response2.createdAt());
            Assertions.assertThat(response2.createdAt()).isAfter(response3.createdAt());
        }


        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 목록 좋아요 순 정렬 조회시, 가장 좋아요가 많은 게시물이 첫 번째로 보인다.")
        void read_boards_order_by_likes(
                final Board board1,
                final Board board2,
                final Member member) throws Exception {

            //given
            given(memberRepository.save(member)).willReturn(member);
            memberRepository.save(member);

            final Like like = likeMapper.registerLikeRequestToEntity(member, board2);
            given(likeRepository.save(like)).willReturn(like);
            likeRepository.save(like);

            final Slice<Board> boards = new SliceImpl<>(List.of(board2, board1));
            given(boardRepository.findAllByOrderByLikesDesc(pageable)).willReturn(boards);

            //when
            final var sortedBoards = boardServiceImpl.getSortedBoards("likes", pageable);

            //then
            final var mostLikedBoard = sortedBoards.getContent().get(0);
            Assertions.assertThat(sortedBoards.getSize()).isEqualTo(2);
            Assertions.assertThat(mostLikedBoard.title()).isEqualTo(board2.getTitle());
        }
    }

}
