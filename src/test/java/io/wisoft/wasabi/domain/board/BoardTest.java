package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Nested
    @DisplayName("게시글 조회수 증가")
    class IncreaseView {

        @DisplayName("게시글 조회시 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void increase_view_success(final Board board) {

            // given

            // when
            board.increaseView();

            // then
            assertThat(board.getViews()).isEqualTo(1);
        }
    }
}