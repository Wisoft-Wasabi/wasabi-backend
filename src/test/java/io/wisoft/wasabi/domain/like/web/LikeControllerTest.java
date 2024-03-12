package io.wisoft.wasabi.domain.like.web;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.like.web.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.GetLikeResponse;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.exception.LikeNotFoundException;
import io.wisoft.wasabi.domain.member.persistence.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.web.resolver.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.Min;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "likeService")
    private LikeService likeService;

    @MockBean(name = "anonymousLikeService")
    private LikeService anonymousLikeService;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @SpyBean
    private ResponseAspect responseAspect;

    private String accessToken;

    @BeforeEach
    void createToken() {
        accessToken = jwtTokenProvider.createAccessToken(
                1L,
                "wasabi",
                Role.GENERAL,
                false
        );
    }

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void register_like(final RegisterLikeRequest request) throws Exception {

            //given
            final var response = new RegisterLikeResponse(1L);

            given(likeService.registerLike(any(), any())).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    post("/likes")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated());
        }

        @DisplayName("비회원 요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void register_anonymous_like(final RegisterLikeRequest request,
                                     final RegisterLikeResponse response) throws Exception {

            // given
            final UUID sessionId = UUID.randomUUID();
            final MockHttpSession session = new MockHttpSession(null, sessionId.toString());

            given(anonymousLikeService.registerLike(any(), any())).willReturn(response);

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(
                    post("/likes")
                            .content(content)
                            .contentType(APPLICATION_JSON)
                            .session(session));

            // then
            result.andExpect(status().isCreated());
        }

        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        @ParameterizedTest
        @AutoSource
        void register_like_fail(final RegisterLikeRequest request) throws Exception {

            // given
            given(likeService.registerLike(any(), any())).willThrow(new LikeNotFoundException());

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(
                    post("/likes")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(content)
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @DisplayName("요청 시 정상적으로 응답된다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like(final Long boardId,
                         final CancelLikeResponse response) throws Exception {

            // given
            given(likeService.cancelLike(any(), any())).willReturn(response);

            // when
            final var result = mockMvc.perform(
                    delete("/likes")
                            .param("boardId", String.valueOf(boardId))
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isOk());
        }

        @DisplayName("비회원 좋아요 취소 요청 시 정상적으로 취소된다.")
        @ParameterizedTest
        @AutoSource
        void cancel_anonymous_like(@Min(1) final Long boardId,
                                   final CancelLikeResponse response) throws Exception {

            // given
            final UUID sessionId = UUID.randomUUID();
            final MockHttpSession session = new MockHttpSession(null, sessionId.toString());

            given(anonymousLikeService.cancelLike(any(), any())).willReturn(response);

            // when
            final var result = mockMvc.perform(
                    delete("/likes")
                            .param("boardId", String.valueOf(boardId))
                            .session(session));

            // then
            result.andExpect(status().isOk());
        }

        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like_fail(final Long boardId) throws Exception {

            // given
            given(likeService.cancelLike(any(), any())).willThrow(new LikeNotFoundException());

            // when
            final var result = mockMvc.perform(
                    delete("/likes")
                            .param("boardId", String.valueOf(boardId))
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {

        @DisplayName("요청 시 정상적으로 조회되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void get_like_status(final Long boardId) throws Exception {

            //given
            final var response = new GetLikeResponse(false, 0);

            given(likeService.getLikeStatus(any(), any())).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    get("/likes")
                            .param("boardId", String.valueOf(boardId))
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            //then
            result.andExpect(status().isOk());
        }

        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        @ParameterizedTest
        @AutoSource
        void get_like_status_fail(final Long boardId) throws Exception {

            //given
            given(likeService.getLikeStatus(any(), any())).willThrow(new LikeNotFoundException());

            //when
            final var result = mockMvc.perform(
                    get("/likes")
                            .param("boardId", String.valueOf(boardId))
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            //then
            result.andExpect(status().isNotFound());
        }
    }
}