package io.wisoft.wasabi.domain.auth.dto.response;

import io.wisoft.wasabi.global.enumeration.Role;

public record LoginResponse(String name, Role role, boolean activation, String accessToken, String tokenType) {

}
