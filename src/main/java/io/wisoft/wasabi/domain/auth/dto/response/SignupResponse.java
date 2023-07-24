package io.wisoft.wasabi.domain.auth.dto.response;

import io.wisoft.wasabi.domain.member.Member;

public record SignupResponse(Long id, String name) {

    public SignupResponse(final Member member) {
        this(member.getId(), member.getName());
    }
}
