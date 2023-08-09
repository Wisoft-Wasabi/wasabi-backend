package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
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

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @Nested
    @DisplayName("회원 개인 정보 수정")
    class UpdateInfo {

        @ParameterizedTest
        @AutoSource
        @DisplayName("회원이 자신의 개인 정보를 수정시 정상적으로 반영된다.")
        void update_info(final Member member) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final var request = new UpdateMemberInfoRequest(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    Part.BACKEND,
                    "organization",
                    "motto");

            // when
            memberServiceImpl.updateMemberInfo(member.getId(), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(member.getUpdatedAt()).isAfter(member.getCreatedAt());
                softly.assertThat(member.getPart()).isEqualTo(Part.BACKEND);
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("직군에 설정되어 있지 않는 값이 들어올시 기본 직군으로 수정된다.")
        void update_info_default(final Member member) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final var request = new UpdateMemberInfoRequest(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    null,
                    "organization",
                    "motto");

            // when
            memberServiceImpl.updateMemberInfo(member.getId(), request);

            // then
            assertSoftly(softly -> softly.assertThat(member.getPart()).isEqualTo(Part.UNDEFINED));
        }

        @Test
        @DisplayName("존재하지 않는 사용자의 개인 정보 수정 접근시 예외가 발생한다.")
        void update_info_fail() {

            // given
            final var request = new UpdateMemberInfoRequest(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    Part.BACKEND,
                    "organization",
                    "motto");

            // when

            // then
            assertSoftly(softly -> softly.assertThatThrownBy(() -> memberServiceImpl.updateMemberInfo(1000L, request))
                    .isInstanceOf(MemberNotFoundException.class));
        }
    }
}