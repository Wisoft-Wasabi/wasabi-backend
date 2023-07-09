package io.wisoft.wasabi.domain.auth.dto;

import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.global.response.dto.DataResponse;

public record MemberSignupResponseDto(Long id, String email) implements DataResponse {

    public MemberSignupResponseDto(Member member) {
        this(member.getId(), member.getEmail());
    }
}
