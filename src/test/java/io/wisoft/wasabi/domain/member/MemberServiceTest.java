package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

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
            memberService.updateMemberInfo(member.getId(), request);

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
            memberService.updateMemberInfo(member.getId(), request);

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
            assertSoftly(softly -> softly.assertThatThrownBy(() -> memberService.updateMemberInfo(1000L, request))
                    .isInstanceOf(MemberNotFoundException.class));
        }
    }

    @Nested
    @DisplayName("개인 정보 조회")
    class ReadMemberInfo {

        @AutoSource
        @ParameterizedTest
        @DisplayName("자신의 개인 정보 조회에 성공한다.")
        void read_member_info_success(final Member member) throws Exception {

            //given
            final Long memberId = 1L;
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            final var mockResponse = new ReadMemberInfoResponse(
                    member.getEmail(),
                    member.getName(),
                    member.getPhoneNumber(),
                    member.getRole(),
                    member.getReferenceUrl(),
                    member.getPart(),
                    member.getOrganization(),
                    member.getMotto()
            );
            given(memberMapper.entityToReadMemberInfoResponse(member)).willReturn(mockResponse);

            //when
            final var response = memberService.getMemberInfo(memberId);

            //then
            assertThat(response.name()).isEqualTo(mockResponse.name());
            assertThat(response.email()).isEqualTo(mockResponse.email());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("토큰에 실린 id가 유효하지 않을 경우, 정보 조회에 실패한다.")
        void read_member_info_fail(final Member member) throws Exception {

            //given
            final Long invalidId = 100000L;
            given(memberRepository.findById(any())).willThrow(new MemberNotFoundException());

            //when

            //then
            Assertions.assertThrows(MemberNotFoundException.class, () -> {
                memberService.getMemberInfo(invalidId);
            });
        }
    }
}