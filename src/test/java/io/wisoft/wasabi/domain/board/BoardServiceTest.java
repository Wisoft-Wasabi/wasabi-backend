package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;


    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        public void 성공() throws Exception {

            //given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "게시글조회성공@email.com",
                            "pass12",
                            "pass12",
                            "name",
                            "phoneNumber",
                            Role.GENERAL
                    )
            );

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );

            when(boardRepository.findById(any())).thenReturn(Optional.of(board));

            //when
            final var response = boardService.readBoard(board.getId());

            //then
            Assertions.assertThat(response.views()).isEqualTo(1L);
        }
    }
}