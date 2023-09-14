package io.wisoft.wasabi.domain.auth.dto;

import io.wisoft.wasabi.domain.member.Role;

public record LoginResponse(String name, Role role, boolean activation, String accessToken, String tokenType) {

}
