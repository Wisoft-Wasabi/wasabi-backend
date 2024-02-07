package io.wisoft.wasabi.domain.comment;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.like.application.LikeRepository;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentIntegrationTest extends IntegrationTest {

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

    @Nested
    @DisplayName("댓글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_comment(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId(), member.getName(), member.getRole(), member.isActivation());

            final Board board = new Board(
                    "title",
                    "content",
                    member
            );

            final Board savedBoard = boardRepository.save(board);


            final WriteCommentRequest request = new WriteCommentRequest(
                    savedBoard.getId(),
                    "content"
            );

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(post("/comments")
                    .contentType(APPLICATION_JSON)
                    .header(Const.AUTH_HEADER,  Const.TOKEN_TYPE + " " + accessToken)
                    .content(json));

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.commentId").exists());
        }

        @DisplayName("요청시 로그인 상태여야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board_fail1(final Member member) throws Exception {

            // given
            memberRepository.save(member);
            final Board board = new Board(
                    "title",
                    "content",
                    member
            );
            boardRepository.save(board);

            final WriteCommentRequest request = new WriteCommentRequest(
                    board.getId(),
                    "content"
            );

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(post("/comments")
                    .contentType(APPLICATION_JSON)
                    .content(json));

            // then
            result.andExpect(status().isUnauthorized());
        }

        @DisplayName("요청시 댓글 내용은 필수다.")
        @ParameterizedTest
        @AutoSource
        void write_post_fail2(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId(), savedMember.getName(), savedMember.getRole(), member.isActivation());

            final Board board = new Board(
                    "title",
                    "content",
                    member
            );

             boardRepository.save(board);

            final WriteCommentRequest request = new WriteCommentRequest(
                    board.getId(),
                    null
            );

            final String json = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(post("/comments")
                    .contentType(APPLICATION_JSON)
                    .header(Const.AUTH_HEADER,  Const.TOKEN_TYPE + " " + accessToken)
                    .content(json));

            // then
            result.andExpect(status().isBadRequest());
        }
    }
    
}
