package io.wisoft.wasabi.domain.like;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.like.dto.CancelLikeRequest;
import io.wisoft.wasabi.domain.like.dto.CancelLikeResponse;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.like.exception.LikeNotFoundException;
import io.wisoft.wasabi.global.config.MemberIdResolver;
import io.wisoft.wasabi.global.enumeration.Role;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LikeService likeService;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @Test
        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        void register_like() throws Exception {

            //given
            final String token = jwtTokenProvider.createAccessToken(1L, "wasabi", Role.GENERAL);

            final var request = new RegisterLikeRequest(1L);

            final var response = new RegisterLikeResponse(1L);
            given(likeService.registerLike(any(), any())).willReturn(response);


            //when
            final var result = mockMvc.perform(post("/likes")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "Bearer" + token)
                    .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

//        @Test
        @DisplayName("요청 시 정상적으로 응답된다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like(
                final CancelLikeRequest request,
                final CancelLikeResponse response
        ) throws Exception {

            // given
            final String token = jwtTokenProvider.createAccessToken(1L, "wasabi", Role.GENERAL);

            given(likeService.cancelLike(any(), any())).willReturn(response);

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isOk());
        }

//        @Test
        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like_fail(
                final CancelLikeRequest request,
                final LikeNotFoundException exception
        ) throws Exception {

            // given
            final String token = jwtTokenProvider.createAccessToken(1L, "wasabi", Role.GENERAL);

            given(likeService.cancelLike(any(), any())).willThrow(exception);

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isNotFound());
        }
    }
}