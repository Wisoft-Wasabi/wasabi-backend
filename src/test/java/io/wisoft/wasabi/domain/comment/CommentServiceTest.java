package io.wisoft.wasabi.domain.comment;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @Nested
    @DisplayName("댓글 작성")
    class WriteComment {

        @DisplayName("요청 시 댓글 작성 저장이 수행된다.")
        @ParameterizedTest
        @AutoSource
        void write_board_with_tag(final Member member, final Board board) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            final var request = new WriteCommentRequest(
                    1L,
                    "content");

            final var comment = CommentMapper.writeCommentRequestToEntity(request, member, board);

            given(commentRepository.save(any())).willReturn(comment);

            // when
            final var response = commentServiceImpl.writeComment(request, member.getId());

            // then
            assertThat(response.commentId()).isEqualTo(comment.getId());
        }
    }
}
