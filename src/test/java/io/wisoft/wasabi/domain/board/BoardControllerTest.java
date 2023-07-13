package io.wisoft.wasabi.domain.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clear() {
        memberRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @Test
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_board() throws Exception {

            // given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "test@gmail.com",
                            "test1234",
                            "test1234",
                            "name",
                            "01000000000",
                            Role.GENERAL));
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createMemberToken(savedMember);

            final WriteBoardRequest request = new WriteBoardRequest(
                    savedMember.getId(),
                    "title",
                    "content",
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // expected
            mockMvc.perform(post("/api/boards")
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "bearer " + accessToken)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dataResponse.id").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("요청시 로그인 상태여야 한다.")
        void write_board_fail1() throws Exception {

            // given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "test@gmail.com",
                            "test1234",
                            "test1234",
                            "name",
                            "01000000000",
                            Role.GENERAL));
            final Member savedMember = memberRepository.save(member);

            final WriteBoardRequest request = new WriteBoardRequest(
                    savedMember.getId(),
                    "title",
                    "content",
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // expected
            mockMvc.perform(post("/api/boards")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }

        @Test
        @DisplayName("요청시 제목과 본문은 필수다.")
        void write_post_fail2() throws Exception {

            // given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "test@gmail.com",
                            "test1234",
                            "test1234",
                            "name",
                            "01000000000",
                            Role.GENERAL));
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createMemberToken(savedMember);

            final WriteBoardRequest request = new WriteBoardRequest(
                    savedMember.getId(),
                    "    ",
                    null,
                    new String[]{"imageUrls"});

            final String json = objectMapper.writeValueAsString(request);

            // expected
            mockMvc.perform(post("/api/boards")
                            .contentType(APPLICATION_JSON)
                            .header("Authorization", "bearer " + accessToken)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}