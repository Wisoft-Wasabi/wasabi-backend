package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.global.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.enumeration.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MemberMapper {
    private final EncryptHelper encryptHelper;

    @Value("${bcrypt.secret.salt}")
    private String salt;

    public MemberMapper(final EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public Member createMemberFromRequest(final SignupRequest request) {
        return new Member(
                request.email(),
                encryptHelper.encrypt(request.password(), salt),
                request.name(),
                request.phoneNumber(),
                false,
                Role.GENERAL
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
}
