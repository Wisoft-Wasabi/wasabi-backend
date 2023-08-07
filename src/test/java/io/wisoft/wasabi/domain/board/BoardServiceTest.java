package io.wisoft.wasabi.domain.board;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Spy
    private BoardMapper boardMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WriteBoard {

        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void write_board(final Member member) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final Board board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.save(any())).willReturn(board);

            // when
            final WriteBoardResponse response = boardServiceImpl.writeBoard(request, 1L);

            // then
            assertEquals("title", response.title());
            assertNotNull(response);
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class ReadBoard {

        @DisplayName("요청이 성공적으로 수행되어, 조회수가 1 증가해야 한다.")
        @ParameterizedTest
        @AutoSource
        void read_board_success(final Member member) throws Exception {

            //given
            final WriteBoardRequest request = new WriteBoardRequest(
                    "title",
                    "content",
                    new String[]{"tags"},
                    new String[]{"imageUrls"});

            final Board board = boardMapper.writeBoardRequestToEntity(request, member);
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            //when
            final var response = boardServiceImpl.readBoard(board.getId());

            //then
            Assertions.assertThat(response.views()).isEqualTo(1L);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("좋아요한 게시글 목록 조회 요청시 자신이 좋아요를 누른 게시글 목록이 최신순으로 조회된다.")
        @Customization(NotSaveBoardCustomization.class)
        void read_my_like_boards(final Long memberId,
                                 final Board board1,
                                 final Board board2,
                                 final Board board3) {

            // given
            final var boards = new SliceImpl<>(List.of(board3, board2, board1));
            given(boardRepository.findAllMyLikeBoards(any(), any())).willReturn(boards);

            // when
            final var pageable = PageRequest.of(0, 3);
            final var myLikeBoards = boardServiceImpl.getMyLikeBoards(memberId, pageable);

            // then
            assertSoftly(softly -> {
                final var response1 = myLikeBoards.getContent().get(0);
                final var response2 = myLikeBoards.getContent().get(1);
                final var response3 = myLikeBoards.getContent().get(2);

                softly.assertThat(myLikeBoards.getSize()).isEqualTo(3);
                softly.assertThat(response1.createAt()).isAfter(response2.createAt());
                softly.assertThat(response2.createAt()).isAfter(response3.createAt());
            });
        }
    }
}
