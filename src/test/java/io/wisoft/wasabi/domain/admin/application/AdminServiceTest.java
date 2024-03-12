package io.wisoft.wasabi.domain.admin.application;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.admin.web.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.web.dto.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.web.dto.DeleteSignUpRequest;
import io.wisoft.wasabi.domain.admin.web.dto.MembersResponse;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberNotFoundException;
import io.wisoft.wasabi.domain.member.persistence.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("승인되지 않은 유저 조회")
    class ReadUnapprovedMembers {

        final Pageable pageable = PageRequest.of(0, 2);

        @DisplayName("승인되지 않은 모든 유저를 조회할 수 있어야 한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void read_unapproved_members(final Member member1,
                                     final Member member2) {

            // given
            final SliceImpl<Member> members = new SliceImpl<>(List.of(member1, member2));

            given(memberRepository.findMemberByUnactivated(any())).willReturn(members);

            final var response = members.map(member -> new MembersResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail()
            ));

            // when
            final var result = adminServiceImpl.getUnapprovedMembers(pageable);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getContent().size()).isEqualTo(response.getSize());
                softAssertions.assertThat(result.getContent().get(0).name()).isEqualTo(response.getContent().get(0).name());
            });
        }
    }

    @Nested
    @DisplayName("승인되지 않은 유저 승인(활성화)")
    class UpdateApproveActivateMember {

        @DisplayName("승인되지 않은 유저를 승인할 수 있다.")
        @ParameterizedTest
        @AutoSource
        void update_approve_activate_member(final Member member) {

            // given
            final var request = new ApproveMemberRequest(member.getId());

            given(memberRepository.findById(any())).willReturn(Optional.of(member));

            final var response = new ApproveMemberResponse(request.memberId());

            // when
            final var result = adminServiceImpl.approveMember(request);

            // then
            assertThat(result.id()).isEqualTo(response.id());
        }

        @DisplayName("존재하지 않는 유저의 승인 요청은 거절된다.")
        @ParameterizedTest
        @AutoSource
        void update_approve_fail_member_not_found(final ApproveMemberRequest request) {

            // given
            given(memberRepository.findById(any())).willReturn(Optional.empty());

            // when

            // then
            assertThrows(MemberNotFoundException.class,
                    () -> adminServiceImpl.approveMember(request));
        }
    }

    @Nested
    @DisplayName("회원 가입 요청 삭제")
    class DeleteSignUp {

        @DisplayName("회원 가입 요청들을 성공적으로 삭제된다.")
        @ParameterizedTest
        @AutoSource
        void delete_sign_up_requests(final DeleteSignUpRequest request) {

            // given
            given(memberRepository.deleteAllByMemberIds(any())).willReturn(request.ids().size());

            // when
            final var result = adminServiceImpl.deleteSignUp(request);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.deletedCount()).isEqualTo(request.ids().size());
            });
        }
    }
}