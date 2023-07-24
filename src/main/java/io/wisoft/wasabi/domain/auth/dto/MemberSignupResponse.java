package io.wisoft.wasabi.domain.auth.dto;

import io.wisoft.wasabi.domain.member.Member;

public record MemberSignupResponse(Long id, String name) {

    public MemberSignupResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
