package io.wisoft.wasabi.domain.auth.dto.response;

import io.wisoft.wasabi.global.enumeration.Role;
import io.wisoft.wasabi.global.response.dto.DataResponse;

public record MemberLoginResponse(String name, Role role, boolean activation, String accessToken, String tokenType) implements DataResponse {

}
