package io.wisoft.wasabi.domain.member.dto;

import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.domain.member.Role;

public record ReadMemberInfoResponse(
        String email,
        String name,
        String phoneNumber,
        Role role,
        String referenceUrl,
        Part part,
        String organization,
        String motto) {
}
