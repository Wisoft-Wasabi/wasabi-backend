package io.wisoft.wasabi.domain.admin.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeleteSignUpRequest(
    @NotNull
    List<Long> ids
) {
}
