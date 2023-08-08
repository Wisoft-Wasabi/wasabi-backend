package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MemberMapper {
    private final EncryptHelper encryptHelper;

    @Value("${bcrypt.secret.salt}")
    private String salt;

    public MemberMapper(final EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public Member signUpRequestToEntity(final SignupRequest request) {

        return new Member(
                request.email(),
                encryptHelper.encrypt(request.password(), salt),
                request.name(),
                request.phoneNumber(),
                false,
                Role.GENERAL,
                convertEmptyToNull(request.referenceUrl()),
                request.part(),
                convertEmptyToNull(request.organization()),
                convertEmptyToNull(request.motto())
        );
    }

    public SignupResponse entityToMemberSignupResponse(final Member member) {
        return new SignupResponse(
                member.getId(),
                member.getName()
        );
    }

    public LoginResponse mapToLoginResponse(final Member member, final String accessToken) {
        final String tokenType = "bearer";
        final String name = member.getName();
        final Role role = member.getRole();
        final boolean activation = member.isActivation();

        return new LoginResponse(name, role, activation, accessToken, tokenType);
    }

    public String convertEmptyToNull(final String value) {
        return StringUtils.hasText(value) ? null : value;
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
}
