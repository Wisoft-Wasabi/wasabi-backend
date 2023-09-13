package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.domain.auth.exception.TokenNotExistException;
import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.domain.like.LikeService;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.annotation.AnyoneResolver;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.interceptor.AdminInterceptor;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private LikeService likeService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @MockBean
    private AdminInterceptor adminInterceptor;

    @MockBean
    private AnyoneResolver anyoneResolver;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private BoardMapper boardMapper;

    @SpyBean
    private ResponseAspect responseAspect;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @Test
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_board() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL, true);

            final var request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final var response = new WriteBoardResponse(
                    1L,
                    "title",
                    "writer"
            );
            given(boardService.writeBoard(any(), any())).willReturn(response);

            // when
            final var perform = mockMvc.perform(
                    post("/boards")
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken)
                            .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated());
        }
    }


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        void read_board_success(final ReadBoardResponse.Writer writer) throws Exception {

            //given
            final Long boardId = 1L;

            final var response = new ReadBoardResponse(
                    1L,
                    "title",
                    "content",
                    writer,
                    LocalDateTime.now(),
                    0,
                    1,
                    false,
                    null
            );

            given(boardService.readBoard(any(), any())).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    get("/boards/{boardId}", boardId)
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON));

            //then
            result.andExpect(status().isOk());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 조회수 순 정렬 후 조회시, 조회수 가장 많은 게시글이 먼저 조회된다.")
        @Customization(NotSaveBoardCustomization.class)
        void read_boards_order_by_views(
                final Board board1,
                final Board board2) throws Exception {

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

            given(boardService.getBoardList(any(), any())).willReturn(boardList);

            //when
            final var result = mockMvc.perform(
                    get("/boards?sortBy=views")
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON));

            //then
            result.andExpect(status().isOk());
        }

        @DisplayName("게시글 조회수 순 정렬 후 조회시, 최신순으로 게시글이 먼저 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void read_boards_order_by_created_at(final Board board1,
                                             final Board board2) throws Exception {
            //given
            final var boards = new SliceImpl<>(List.of(board2, board1));

            final var boardList = boards.map(board -> new SortBoardResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getMember().getName(),
                    board.getCreatedAt(),
                    board.getLikes().size(),
                    board.getViews()
            ));

            given(boardService.getBoardList(any(), any())).willReturn(boardList);

            //when
            final var result = mockMvc.perform(
                    get("/boards?sortBy=latest")
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
            );

            //then
            result.andExpect(status().isOk());
        }

        @DisplayName("게시글 좋아요 순 정렬 후 조회시, 좋아요가 가장 많은 게시글이 먼저 조회된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void read_boards_order_by_likes(
                final Board board1,
                final Board board2,
                final Member member) throws Exception {
            //given
            likeService.registerLike(member.getId(), new RegisterLikeRequest(board1.getId()));

            final var boards = new SliceImpl<>(List.of(board2, board1));

            final var boardList = boards.map(board -> new SortBoardResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getMember().getName(),
                    board.getCreatedAt(),
                    board.getLikes().size(),
                    board.getViews()
            ));

            given(boardService.getBoardList(any(), any())).willReturn(boardList);

            //when
            final var result = mockMvc.perform(
                    get("/boards?sortBy=likes")
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
            );

            //then
            result.andExpect(status().isOk());
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시글 목록이 반환된다.")
        @ParameterizedTest
        @AutoSource
        void read_my_boards(
                final List<MyBoardsResponse> boardsResponses
        ) throws Exception {

            // given
            given(boardService.getMyBoards(any(), any())).willReturn(new SliceImpl<>(boardsResponses));

            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL, true);

            // when
            final var result = mockMvc.perform(
                    get("/boards/my-board")
                            .param("page", String.valueOf(0))
                            .param("size", String.valueOf(3))
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken)
            );

            // then
            result.andExpect(status().isOk());
        }


        @DisplayName("로그인 하지 않은 사용자가 좋아요한 게시글 목록 조회 요청시 예외가 발생한다.")
        @ParameterizedTest
        @AutoSource
        void read_my_like_boards_fail(final TokenNotExistException exception) throws Exception {

            // given
            given(boardService.getMyLikeBoards(any(), any())).willThrow(exception);

            // when
            final var result = mockMvc.perform(
                    get("/boards/my-like")
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isUnauthorized());
        }

        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글 목록이 반환된다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void read_my_like_boards(final List<Board> boards) throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL, true);

            final var response = boardMapper.entityToMyLikeBoardsResponse(new SliceImpl<>(boards));

            given(boardService.getMyLikeBoards(any(), any())).willReturn(response);

            // when
            final var result = mockMvc.perform(
                    get("/boards/my-like")
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));

            // then
            result.andExpect(status().isOk());
        }
    }
}