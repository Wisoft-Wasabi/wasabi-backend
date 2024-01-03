package io.wisoft.wasabi.domain.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentResponse;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.annotation.AnyoneResolver;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.interceptor.AdminInterceptor;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @SpyBean
    private ResponseAspect responseAspect;

    @Nested
    @DisplayName("댓글 작성")
    class WriteComment{

        @Test
        @DisplayName("댓글 작성 요청시 정상적으로 저장되어야 한다.")
        void write_comment() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL, true);

            final var request = new WriteCommentRequest(
                    1L,
                    "content");

            final var response = new WriteCommentResponse(
                    1L,
                    1L
            );

            given(commentService.writeComment(any(), any())).willReturn(response);

            // when
            final var perform = mockMvc.perform(
                    post("/comments")
                            .contentType(APPLICATION_JSON)
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated());
        }
    }
}
