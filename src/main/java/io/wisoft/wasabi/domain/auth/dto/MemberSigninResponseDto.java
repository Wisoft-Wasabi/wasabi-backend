package io.wisoft.wasabi.domain.auth.dto;

import io.wisoft.wasabi.global.response.dto.DataResponse;

public record MemberSigninResponseDto(String accessToken, String tokenType) implements DataResponse {

}
