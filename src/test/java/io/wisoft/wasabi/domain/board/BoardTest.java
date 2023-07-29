package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

class BoardTest {


    @Nested
    @DisplayName("게시글 조회수 증가")
    class IncreaseView {

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시글 조회시 조회수가 1 증가해야 한다.")
        public void increase_view_success(final Member member) {

            //given -- 조건
            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );

            //when -- 동작
            board.increaseView();

            //then -- 검증
            Assertions.assertThat(board.getViews()).isEqualTo(1);
        }
    }
}