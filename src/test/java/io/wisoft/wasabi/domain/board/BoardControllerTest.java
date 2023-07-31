package io.wisoft.wasabi.domain.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

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
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @Test
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_board() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL);

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
            final var perform = mockMvc.perform(post("/boards")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated());
        }
    }


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글 조회에 성공한다.")
        void read_board_success() throws Exception {

            //given
            final Long boardId = 1L;

            final var response = new ReadBoardResponse(
                    1L,
                    "title",
                    "content",
                    "test-member-name",
                    LocalDateTime.now(),
                    0,
                    1,
                    false,
                    null
            );

            given(boardService.readBoard(boardId)).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    get("/boards/{boardId}", boardId)
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON));

            //then
            result.andExpect(status().isOk());
        }
    }
}