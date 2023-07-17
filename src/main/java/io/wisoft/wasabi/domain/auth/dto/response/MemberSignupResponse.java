package io.wisoft.wasabi.domain.auth.dto.response;

import io.wisoft.wasabi.domain.member.persistence.Member;

public record MemberSignupResponse(Long id, String name) {

    public MemberSignupResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
