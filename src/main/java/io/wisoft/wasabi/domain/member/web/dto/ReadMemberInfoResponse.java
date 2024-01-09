package io.wisoft.wasabi.domain.member.web.dto;

import io.wisoft.wasabi.domain.member.persistence.Part;
import io.wisoft.wasabi.domain.member.persistence.Role;

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
