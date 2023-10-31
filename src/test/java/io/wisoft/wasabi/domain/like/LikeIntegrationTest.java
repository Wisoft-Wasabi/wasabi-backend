package io.wisoft.wasabi.domain.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    private Member member;

    private Board board;

    @BeforeEach
    void init() {
        final int random = new Random().nextInt(100_000);
        member = new Member(
                "게시글작성성공" + random + "@gmail.com",
                "test1234",
                "test1234",
                "01000000000",
                false,
                Role.GENERAL,
                "www.naver.com",
                Part.BACKEND,
                "wisoft",
                "공부는 동엽이처럼");

        memberRepository.save(member);

        board = new Board(
                "title",
                "content",
                member
        );

        boardRepository.save(board);
    }

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @Test
        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        void register_like() throws Exception {

            //given
            final String accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            final var request = new RegisterLikeRequest(board.getId());

            final String content = objectMapper.writeValueAsString(request);

            //when
            final var result = mockMvc.perform(post("/likes")
                    .contentType(APPLICATION_JSON)
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                    .content(content));

            //then
            result.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("비회원 요청 시 정상적으로 등록되어야 한다.")
        void register_anonymous_like() throws Exception {

            // given
            final var request = new RegisterLikeRequest(board.getId());

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(post("/likes")
                    .contentType(APPLICATION_JSON)
                    .content(content));

            // then
            result.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        void register_like_fail() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            final var request = new RegisterLikeRequest(100000L);

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(post("/likes")
                    .contentType(APPLICATION_JSON)
                    .content(content)
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @Test
        @DisplayName("요청시 정상적으로 응답한다.")
        void cancel_like() throws Exception {

            // given
            final Like like = new Like(member, board);
            likeRepository.save(like);

            final String accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .param("boardId", String.valueOf(board.getId()))
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("비회원 요청 시 정상적으로 취소된다.")
        void cancel_anonymous_like() throws Exception {

            // given
            final var request = new RegisterLikeRequest(board.getId());

            final String content = objectMapper.writeValueAsString(request);

            final Cookie sessionCookie =
                    Stream.of(mockMvc.perform(post("/likes")
                                    .contentType(APPLICATION_JSON)
                                    .content(content)).andDo(print()).andReturn().getResponse().getCookies())
                            .filter(cookie -> cookie.getName().equals("SESSION"))
                            .findFirst().get();

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .param("boardId", String.valueOf(board.getId()))
                    .cookie(sessionCookie));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 데이터에 요청할 시 404 에러 응답을 한다.")
        void cancel_like_fail() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .param("boardId", String.valueOf(10L))
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {

        @Test
        @DisplayName("요청 시 정상적으로 조회되어야 한다.")
        void get_like_status() throws Exception {

            //given
            final var accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            //when
            final var result = mockMvc.perform(get("/likes")
                    .param("boardId", String.valueOf(board.getId()))
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            //then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 데이터 요청 시 404 에러를 반환한다.")
        void get_like_status_fail() throws Exception {

            //given
            final var accessToken = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    false
            );

            //when
            final var result = mockMvc.perform(get("/likes")
                    .param("boardId", String.valueOf(100000L))
                    .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            //then
            result.andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}
