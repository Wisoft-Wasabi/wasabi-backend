package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.board.web.dto.WriteBoardRequest;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class BoardIntegrationTest extends IntegrationTest {


    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board(final WriteBoardRequest request) {

            // given
            final String accessToken = adminLogin();

            // when
            final var result = writeBoard(accessToken, request);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @DisplayName("요청시 로그인 상태여야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board_fail1(final WriteBoardRequest request) {

            // given

            // when
            final var result = writeBoard(null, request);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        }

        @DisplayName("요청시 제목과 본문은 필수다.")
        @Test
        void write_post_fail2() {

            // given
            final String accessToken = adminLogin();

            final WriteBoardRequest request = new WriteBoardRequest(
                    "    ",
                    null,
                    "tag",
                    new String[]{"imageUrls"},
                    new ArrayList<>());

            // when
            final var result = writeBoard(accessToken, request);


            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final WriteBoardRequest request) {

            //given
            final String accessToken = adminLogin();
            final int boardId = getDataByKey(writeBoard(accessToken, request), "id");

            //when
            final var result = readBoard((long) boardId);

            //then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @DisplayName("존재하지 않는 게시글을 조회하려 할 경우, 조회에 실패한다.")
        @Test
        void read_not_found_board() {

            //given

            //when
            final var result = readBoard(Long.MAX_VALUE);

            //then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @DisplayName("작성한 게시글 목록 조회 요청시 자신이 작성한 게시물들이 반환된다.")
        @ParameterizedTest
        @AutoSource
        void read_my_boards(final List<WriteBoardRequest> requests) {

            // given
            final String accessToken = adminLogin();
            requests.forEach(request -> writeBoard(accessToken, request));

            // when
            final var result = readMyBoards(accessToken);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요한 게시물들이 반환된다.")
        @ParameterizedTest
        @AutoSource
        void read_my_like_boards(final List<WriteBoardRequest> requests) {

            // given
            final String accessToken = adminLogin();

            final List<Long> boardIds = registerBoards(accessToken, requests);

            final List<Long> likedBoardIds = boardIds.stream()
                .filter(id -> id % 2 == 1)
                .toList();
            likedBoardIds.forEach(boardId -> registerLike(accessToken, boardId));

            // when
            final var result = readMyLikeBoards(accessToken);

            // then
            final List<?> boardList = getDataByKey(result, "content");
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
                softAssertions.assertThat(boardList.size()).isEqualTo(likedBoardIds.size());
            });
        }

        @DisplayName("게시글 좋아요 순 정렬 후 조회시, 좋아요가 많은 게시글이 먼저 조회된다.")
        @ParameterizedTest
        @AutoSource
        void read_boards_order_by_likes(final List<WriteBoardRequest> requests) {
            //given
            final String accessToken = adminLogin();

            final List<Long> boardIds = registerBoards(accessToken, requests);

            final Long likedBoardId = boardIds.get(0);
            registerLike(accessToken, likedBoardId);

            //when
            final var result = readBoardList(0, 3, "likes");

            //then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            // TODO: 컨텐츠 확인
        }

        @DisplayName("게시글 조회수 순 정렬 후 조회시, 조회수가 많은 게시글이 먼저 조회된다.")
        @ParameterizedTest
        @AutoSource
        void read_boards_order_by_views(final List<WriteBoardRequest> requests) {

            //given
            final String accessToken = adminLogin();

            final List<Long> boardIds = registerBoards(accessToken, requests);

            final Long mostViewedBoardId = boardIds.get(0);
            readBoard(mostViewedBoardId);

            //when
            final var result = readBoardList(0, 3, "views");

            //then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            // TODO: 컨텐츠 확인
        }

        @DisplayName("게시글 최신 순 정렬 후 조회시, 최신에 작성한 게시글이 먼저 조회된다.")
        @ParameterizedTest
        @AutoSource
        void read_boards_order_by_created_at(final List<WriteBoardRequest> requests) {

            //given
            final String accessToken = adminLogin();

            final List<Long> boardIds = registerBoards(accessToken, requests);

            final Long latestRegisterBoardId = boardIds.get(requests.size() - 1);

            //when
            final var result = readBoardList(0, 3, "latest");

            //then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            // TODO: 컨텐츠 확인
        }

        private List<Long> registerBoards(final String accessToken,
                                          final List<WriteBoardRequest> requests) {

            return requests.stream()
                .map(request -> writeBoard(accessToken, request))
                .map(response -> (int) getDataByKey(response, "id"))
                .map(id -> (long) id)
                .toList();
        }
    }
}