package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.Role;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @Test
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_board() {

            // given
            final Member member = Member.createMember(
                    new MemberSignupRequestDto(
                            "test@gmail.com",
                            "test1234",
                            "test1234",
                            "name",
                            "01000000000",
                            Role.GENERAL));
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final WriteBoardRequest request = new WriteBoardRequest(
                    member.getId(),
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            given(boardRepository.save(any())).willReturn(board);

            // when
            final WriteBoardResponse response = boardServiceImpl.writeBoard(request);

            // then
            assertEquals("title", response.title());
            assertEquals("name", response.writer());
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        void read_board_success() throws Exception {

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
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            //when
            final var response = boardServiceImpl.readBoard(board.getId());

            //then
            Assertions.assertThat(response.views()).isEqualTo(1L);
        }
    }
}
