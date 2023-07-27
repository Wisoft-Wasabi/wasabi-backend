package io.wisoft.wasabi.domain.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.like.dto.CancelLikeRequest;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.global.enumeration.Part;
import io.wisoft.wasabi.global.enumeration.Role;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    private Board board;

    @BeforeEach
    void init() {
        final int random = new Random().nextInt(100_000);
        member = Member.createMember(
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

        board = Board.createBoard(
                "title",
                "content",
                member);
        boardRepository.save(board);
    }

    @Nested
    @DisplayName("게시글 취소")
    class CancelLike {

        @Test
        @DisplayName("요청시 정상적으로 응답한다.")
        void cancel_like() throws Exception {

            // given
            final Like like = Like.createLike(member, board);
            likeRepository.save(like);

            final String token = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole()
            );

            final var request = new CancelLikeRequest(board.getId());
            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("Authorization", "Bearer " + token));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 데이터에 요청할 시 404 에러 응답을 한다.")
        void cancel_like_fail() throws Exception {

            // given
            final String token = jwtTokenProvider.createAccessToken(
                    member.getId(),
                    member.getName(),
                    member.getRole()
            );

            final var request = new CancelLikeRequest(10L);
            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(delete("/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("Authorization", "Bearer " + token));

            // then
            result.andExpect(status().isNotFound());
        }

    }

}
