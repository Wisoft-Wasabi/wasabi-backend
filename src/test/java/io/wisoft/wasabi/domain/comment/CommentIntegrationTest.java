package io.wisoft.wasabi.domain.comment;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.board.web.dto.WriteBoardRequest;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentIntegrationTest extends IntegrationTest {

    @Nested
    @DisplayName("댓글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_comment(final WriteBoardRequest writeBoardRequest) throws Exception {

            // given
            final String accessToken = adminLogin();

            final int boardId = getDataByKey(writeBoard(accessToken, writeBoardRequest), "id");

            // when
            final var result = writeComment(accessToken, (long) boardId, "test content");

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @DisplayName("요청시 로그인 상태여야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board_fail1(final WriteBoardRequest writeBoardRequest) throws Exception {

            // given
            final String accessToken = adminLogin();

            final int boardId = getDataByKey(writeBoard(accessToken, writeBoardRequest), "id");

            // when
            final var result = writeComment(null, (long) boardId, "test content");

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @DisplayName("요청시 댓글 내용은 필수다.")
        @ParameterizedTest
        @AutoSource
        void write_post_fail2(final WriteBoardRequest writeBoardRequest) throws Exception {

            // given
            final String accessToken = adminLogin();

            final int boardId = getDataByKey(writeBoard(accessToken, writeBoardRequest), "id");

            // when
            final var result = writeComment(accessToken, (long) boardId, null);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
    
}
