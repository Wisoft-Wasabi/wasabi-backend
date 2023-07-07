package io.wisoft.wasabi.domain.auth.dto;

public record LoginResponse(
        String accessToken,
        String TokenType) {
}
