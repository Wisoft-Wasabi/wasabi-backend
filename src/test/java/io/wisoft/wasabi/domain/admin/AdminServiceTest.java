package io.wisoft.wasabi.domain.admin;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.DeleteSignUpRequest;
import io.wisoft.wasabi.domain.admin.dto.MembersResponse;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import org.assertj.core.api.SoftAssertions;
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
        void read_unapproved_members(final Member member1, final Member member2) {

            // given
            final var members = new SliceImpl<>(List.of(member1, member2));

            given(memberRepository.findMemberByUnactivated(pageable)).willReturn(members);

            final var mockResponse = members.map(member -> new MembersResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail()
            ));

            // when
            final var unapprovedMembers = adminServiceImpl.getUnapprovedMembers(pageable);

            // then
            assertThat(unapprovedMembers.getContent()).hasSize(members.getContent().size());
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
            final ApproveMemberRequest request = new ApproveMemberRequest(member.getId());

            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            // when
            final var approveMemberResponse = adminServiceImpl.approveMember(request);

            // then
            assertThat(approveMemberResponse.id()).isEqualTo(member.getId());
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
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.deletedCount()).isEqualTo(request.ids().size());
            });
        }
    }
}