package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.setting.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        boardRepository.deleteAll();
    }


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        public void 성공() throws Exception {

            //given
            final Member member = memberRepository.save(
                    Member.createMember(
                            new MemberSignupRequestDto(
                                    "게시글조회성공@email.com",
                                    "pass12",
                                    "pass12",
                                    "name",
                                    "phoneNumber",
                                    Role.GENERAL
                            )));


            final Board board = boardRepository.save(
                    Board.createBoard(
                            "title",
                            "content",
                            member
                    ));

            //when
            final var result = mockMvc.perform(get("/api/boards/{boardId}", board.getId())
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print());

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.dataResponse.views").value(1));
        }


        @Test
        @DisplayName("존재하지 않는 게시글을 조회하려 할 경우, 조회에 실패한다.")
        public void 실패1() throws Exception {

            //given

            //when
            final var result = mockMvc.perform(get("/api/boards/{boardId}", 125L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            //then
            result.andExpect(status().isNotFound());
        }
    }
}