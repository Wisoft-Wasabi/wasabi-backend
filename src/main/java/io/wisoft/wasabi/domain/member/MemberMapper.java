package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.auth.dto.SignupRequest;
import io.wisoft.wasabi.global.enumeration.Role;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member signupRequestToEntity(final SignupRequest request) {
        return Member.createMember(
                request.email(),
                request.password(),
                request.name(),
                request.phoneNumber(),
                false,
                Role.GENERAL
        );
    }

}
