package io.wisoft.wasabi.domain.like.application;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.like.persistence.AnonymousLike;
import io.wisoft.wasabi.domain.board.persistence.Board;
import io.wisoft.wasabi.domain.board.application.BoardRepository;
import io.wisoft.wasabi.domain.like.persistence.LikeQueryRepository;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnonymousLikeServiceImplTest {

    @InjectMocks
    private AnonymousLikeServiceImpl anonymousLikeService;

    @Mock
    private AnonymousLikeRepository anonymousLikeRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LikeQueryRepository likeQueryRepository;

    @Nested
    @DisplayName("비회원 좋아요 등록")
    class RegisterAnonymousLike {

        @DisplayName("요청 시 정상적으로 등록되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void register_anonymous_like(final Board board,
                                     final RegisterLikeRequest request,
                                     @Min(1) final Long accessId) {

            // given
            given(boardRepository.findById(any())).willReturn(Optional.of(board));

            // when
            final var result = anonymousLikeService.registerLike(accessId, request);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("비회원 좋아요 취소")
    class CancelAnonymousLike {

        @DisplayName("요청 시 정상적으로 취소되어야 한다.")
        @ParameterizedTest
        @AutoSource
        void cancel_anonymous_like(@Min(1) final Long accessId,
                                   @Min(1) final Long boardId,
                                   final AnonymousLike anonymousLike) {

            // given
            given(anonymousLikeRepository.findBySessionIdAndBoardId(any(), any())).willReturn(Optional.of(anonymousLike));

            // when
            final var result = anonymousLikeService.cancelLike(accessId, boardId);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("비회원 좋아요 상태 조회")
    class GetAnonymousLikeStatus {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청이 성공적으로 수행되어 정상적으로 조회되어야 한다.")
        void get_anonymous_like_status(final Long sessionId,
                                       final Long boardId,
                                       final boolean isLike,
                                       @Min(1) @Max(Integer.MAX_VALUE) final Long likeCount) {

            // given
            given(boardRepository.existsById(any())).willReturn(true);
            given(anonymousLikeRepository.existsBySessionIdAndBoardId(any(), any())).willReturn(isLike);
            given(likeQueryRepository.countByBoardId(any())).willReturn(likeCount);

            // when
            final var result = anonymousLikeService.getLikeStatus(sessionId, boardId);

            // then
            assertThat(result).isNotNull();
        }

    }

}