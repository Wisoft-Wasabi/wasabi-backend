package io.wisoft.wasabi.domain.like.application;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveBoardCustomization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.board.exception.BoardNotFoundException;
import io.wisoft.wasabi.domain.like.exception.ExistLikeException;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.like.exception.LikeNotFoundException;
import io.wisoft.wasabi.domain.like.persistence.Like;
import io.wisoft.wasabi.domain.like.persistence.LikeQueryRepository;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private LikeQueryRepository likeQueryRepository;

    @Nested
    @DisplayName("좋아요 등록")
    class RegisterLike {

        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveBoardCustomization.class)
        void register_like(final Member member,
                           final Board board) {

            //given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            final var request = new RegisterLikeRequest(board.getId());

            final Like like = LikeMapper.registerLikeRequestToEntity(member, board);

            given(likeRepository.save(any())).willReturn(like);

            final var response = new RegisterLikeResponse(like.getId());

            //when
            final var result = likeService.registerLike(member.getId(), request);

            //then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.id()).isEqualTo(response.id());
            });
        }

        @Test
        @DisplayName("존재하지 않는 유저의 좋아요 요청 시 등록되지 않는다.")
        void register_like_fail() {

            //given
            final var request = new RegisterLikeRequest(1L);

            given(memberRepository.findById(any())).willReturn(Optional.empty());

            //when

            //then
            assertThrows(MemberNotFoundException.class,
                    () -> likeService.registerLike(null, request));
        }

        @DisplayName("이미 등록된 좋아요는 중복되어 등록되지 않는다.")
        @ParameterizedTest
        @AutoSource
        void register_like_fail_exist_like(final Member member,
                                           final Board board) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            final var request = new RegisterLikeRequest(board.getId());

            given(likeRepository.existsByMemberIdAndBoardId(any(), any())).willReturn(true);

            // when

            // then
            assertThrows(ExistLikeException.class,
                    () -> likeService.registerLike(member.getId(), request));
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @DisplayName("요청이 성공적으로 수행되어 정상적으로 취소된다.")
        @ParameterizedTest
        @AutoSource
        @Customization({
                NotSaveMemberCustomization.class,
                NotSaveBoardCustomization.class
        })
        void cancel_like(final Member member,
                         final Board board) {

            // given
            final Like like = new Like(member, board);

            given(likeRepository.findByMemberIdAndBoardId(any(), any())).willReturn(Optional.of(like));

            // when
            final var result = likeService.cancelLike(member.getId(), board.getId());

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(like.getMember().getLikes().contains(like)).isFalse();
                softAssertions.assertThat(like.getBoard().getLikes().contains(like)).isFalse();
            });
        }

        @DisplayName("존재하지 않는 좋아요를 조회하여 에러를 던진다.")
        @ParameterizedTest
        @AutoSource
        void cancel_like_fail(final Long boardId) {

            // given
            given(likeRepository.findByMemberIdAndBoardId(any(), any())).willReturn(Optional.empty());

            // when

            // then
            assertThrows(LikeNotFoundException.class,
                    () -> likeService.cancelLike(null, boardId));
        }
    }

    @Nested
    @DisplayName("좋아요 상태 조회")
    class GetLikeStatus {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청이 성공적으로 수행되어 정상적으로 조회되어야 한다.")
        void get_like_status(final Long memberId,
                             final Long boardId) {

            //given
            given(boardRepository.existsById(any())).willReturn(true);
            given(likeQueryRepository.countByBoardId(any())).willReturn(1L);

            //when
            final var response = likeService.getLikeStatus(memberId, boardId);

            //then
            assertThat(response).isNotNull();
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("존재하지 않는 게시글에 대해 좋아요를 조회하면 에러가 발생한다.")
        void get_like_status_fail(final Long memberId,
                                  final Long boardId) {

            //given
            given(boardRepository.existsById(any())).willReturn(false);

            //when

            //then
            assertThrows(BoardNotFoundException.class,
                    () -> likeService.getLikeStatus(memberId, boardId));
        }
    }
}