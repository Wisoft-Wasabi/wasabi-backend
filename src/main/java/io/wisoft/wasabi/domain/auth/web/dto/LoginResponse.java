package io.wisoft.wasabi.domain.auth.web.dto;

import io.wisoft.wasabi.domain.member.persistence.Role;

public record LoginResponse(String name, Role role, boolean activation, String accessToken, String tokenType) {

}
