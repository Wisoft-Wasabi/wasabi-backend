package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.MembersResponse;
import io.wisoft.wasabi.domain.auth.dto.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.SignupResponse;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    private final EncryptHelper encryptHelper;


    public MemberMapper(final EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public Member signUpRequestToEntity(final SignupRequest request) {
        return new Member(
                request.email(),
                encryptHelper.encrypt(request.password()),
                request.name(),
                request.phoneNumber(),
                false,
                Role.GENERAL,
                request.referenceUrl(),
                request.part(),
                request.organization(),
                request.motto()
        );
    }

    public Long approveMemberRequestToMemberId(final ApproveMemberRequest request) {
        return request.memberId();
    }

    public SignupResponse entityToMemberSignupResponse(final Member member) {
        return new SignupResponse(
                member.getId(),
                member.getName()
        );
    }

    public LoginResponse entityToLoginResponse(final Member member, final String accessToken) {
        final String tokenType = Const.TOKEN_TYPE;
        final String name = member.getName();
        final Role role = member.getRole();
        final boolean activation = member.isActivation();

        return new LoginResponse(name, role, activation, accessToken, tokenType);
    }

    public UpdateMemberInfoResponse entityToUpdateMemberInfoResponse(final Member member) {
        return new UpdateMemberInfoResponse(member.getId());
    }

    public ReadMemberInfoResponse entityToReadMemberInfoResponse(final Member member) {

        return new ReadMemberInfoResponse(
                member.getEmail(),
                member.getName(),
                member.getPhoneNumber(),
                member.getRole(),
                member.getReferenceUrl(),
                member.getPart(),
                member.getOrganization(),
                member.getMotto()
        );
    }

    public Slice<MembersResponse> entityToReadMembersInfoResponses(final Slice<Member> members) {

        return members.map(member -> new MembersResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        ));
    }

    public ApproveMemberResponse entityToApproveMemberResponses(final Member member) {
        return new ApproveMemberResponse(member.getId());
    }

}
