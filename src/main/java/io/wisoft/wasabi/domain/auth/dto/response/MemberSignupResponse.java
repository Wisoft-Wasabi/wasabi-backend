package io.wisoft.wasabi.domain.auth.dto.response;

import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.global.response.dto.DataResponse;

public record MemberSignupResponse(Long id, String name) implements DataResponse {

    public MemberSignupResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
