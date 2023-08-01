package io.wisoft.wasabi.domain.like;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.board.Board;
import io.wisoft.wasabi.domain.board.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardNotFoundException;
import io.wisoft.wasabi.domain.like.dto.*;
import io.wisoft.wasabi.domain.like.exception.LikeNotFoundException;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import io.wisoft.wasabi.global.enumeration.Role;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeServiceImpl likeService;

    @Spy
    private LikeMapper likeMapper;

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
            final Member member = Member.createMember(
                    "게시글작성성공@gmail.com",
                    "test1234",
                    "test1234",
                    "01000000000",
                    false,
                    Role.GENERAL);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            final RegisterLikeRequest request = new RegisterLikeRequest(board.getId());

            final Like like = likeMapper.registerLikeRequestToEntity(member, board);
            given(likeRepository.save(any())).willReturn(like);

            //when
            final RegisterLikeResponse response = likeService.registerLike(member.getId(), request);

            //then
            assertNotNull(response);
            assertThat(response.likeId()).isEqualTo(like.getId());
        }

        @Test
        @DisplayName("존재하지 않는 데이터 요청 시 등록되지 않는다.")
        void register_like_fail() throws Exception {

            //given
            final RegisterLikeRequest request = new RegisterLikeRequest(1L);
            given(memberRepository.findById(any())).willReturn(Optional.empty());

            //when

            //then
            assertThrows(
                    MemberNotFoundException.class,
                    () -> likeService.registerLike(null, request)
            );
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @DisplayName("요청이 성공적으로 수행되어 정상적으로 응답한다.")
        @ParameterizedTest
        @AutoSource
        @Customization({
                NotSaveMemberCustomization.class,
                NotSaveBoardCustomization.class
        })
        void cancel_like(
                final Long memberId,
                final Long boardId,
                final Member member,
                final Board board
        ) {

            // given

            final Like like = new Like(member, board);

            given(likeRepository.findByMemberIdAndBoardId(any(), any())).willReturn(Optional.of(like));

            // when
            final CancelLikeResponse result = likeService.cancelLike(memberId, boardId);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result).isNotNull();
                softly.assertThat(like.getMember().getLikes().contains(like)).isFalse();
                softly.assertThat(like.getBoard().getLikes().contains(like)).isFalse();
            });
        }

        @DisplayName("존재하지 않는 좋아요를 조회하여 에러를 던진다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like_fail(
                final Long boardId
        ) {

            // given
            given(likeRepository.findByMemberIdAndBoardId(any(), any())).willReturn(Optional.empty());

            // when

            // then
            Assertions.assertThatThrownBy(
                    () -> likeService.cancelLike(null, boardId)
            ).isInstanceOf(LikeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {
        
        @Test
        @DisplayName("요청이 성공적으로 수행되어 정상적으로 조회되어야 한다.")
        void get_like_status() throws Exception {
            
            //given
            final Long memberId = 1L;
            final Long boardId = 1L;

            final Member member = Member.createMember(
                    "게시글작성성공@gmail.com",
                    "test1234",
                    "test1234",
                    "01000000000",
                    false,
                    Role.GENERAL);
            final Board board = Board.createBoard(
                    "title",
                    "content",
                    member
            );

            given(boardRepository.findById(any())).willReturn(Optional.of(board));
            given(likeRepository.countByBoardId(any())).willReturn(1);

            //when
            final GetLikeResponse response = likeService.getLikeStatus(memberId, boardId);

            //then
            assertThat(response).isNotNull();
        }
        
        @Test
        @DisplayName("존재하지 않는 게시글에 대해 좋아요를 조회하면 에러가 발생한다.")
        void get_like_status_fail() throws Exception {
            
            //given
            final Long memberId = 10L;
            final Long boardId = 10L;

            given(boardRepository.findById(any())).willReturn(Optional.empty());

            //when

            //then
            assertThatThrownBy(
                    () -> likeService.getLikeStatus(memberId, boardId)
            ).isInstanceOf(BoardNotFoundException.class);
        }
    }
}