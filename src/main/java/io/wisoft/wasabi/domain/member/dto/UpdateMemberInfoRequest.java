package io.wisoft.wasabi.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.wisoft.wasabi.domain.member.Part;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.StringUtils;

public record UpdateMemberInfoRequest(
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @Nullable String referenceUrl,
        @Nullable Part part,
        @Nullable String organization,
        @Nullable String motto) {

    @JsonCreator
    public UpdateMemberInfoRequest(final @NotBlank String name,
                                   final @NotBlank String phoneNumber,
                                   final @Nullable String referenceUrl,
                                   final @Nullable Part part,
                                   final @Nullable String organization,
                                   final @Nullable String motto) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.referenceUrl = StringUtils.hasText(referenceUrl) ? referenceUrl : "www.wisoft.io";
        this.part = part == null ? Part.UNDEFINED : part;
        this.organization = StringUtils.hasText(organization) ? organization : "wisoft";
        this.motto = StringUtils.hasText(motto) ? motto : "아자아자";
    }
}
