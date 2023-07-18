package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BoardTest {


    @Nested
    @DisplayName("게시글 조회수 증가")
    class IncreaseView {

        @Test
        @DisplayName("게시글 조회시 조회수가 1 증가해야 한다.")
        public void increase_view_success() {

            //given -- 조건
            final Board board = Board.createBoard(
                    "title",
                    "content",
                    Member.createMember(new MemberSignupRequestDto(
                            "email@email.com",
                            "pass1234",
                            "pass1234",
                            "name",
                            "phoneNumber",
                            Role.GENERAL
                    ))
            );

            //when -- 동작
            board.increaseView();

            //then -- 검증
            Assertions.assertThat(board.getViews()).isEqualTo(1);
        }
    }
}