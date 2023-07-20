package io.wisoft.wasabi.domain.like;

import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.global.enumeration.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LikeRepository likeRepository;

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @Test
        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        void register_like() throws Exception {

            //given
            final Member member = new Member(
                    "test@gmail.com",
                    "test1234",
                    "wasabi",
                    "01012345678",
                    false,
                    Role.GENERAL,
                    LocalDateTime.now()
            );
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            final RegisterLikeRequest request = new RegisterLikeRequest(board.getId());

            final Like like = Like.createLike(board, member);
            given(likeRepository.save(any())).willReturn(like);

            //when
            final RegisterLikeResponse response = likeService.registerLike(member.getId(), request);

            //then
            Assertions.assertThat(response.likeId()).isEqualTo(like.getId());
        }

    }
}