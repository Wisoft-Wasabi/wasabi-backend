package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Stream;

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
                Role.GENERAL,
                convertEmptyToNull(request.referenceUrl()),
//                request.part(),
                convertEmptyToUndefined(String.valueOf(request.part())),
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

    public static String convertEmptyToNull(final String value) {
        return StringUtils.hasText(value) ? null : value;
    }

    public static Part convertEmptyToUndefined(final String value) {
        return (StringUtils.hasText(value)) ? Stream.of(Part.values())
                .filter(part -> part.name().equalsIgnoreCase(value))
                .findFirst().orElse(Part.UNDEFINED) : Part.UNDEFINED;
    }
}
