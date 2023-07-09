package io.wisoft.wasabi.global.response.dto.common;

import io.wisoft.wasabi.global.response.dto.DataResponse;

public record CreateMemberResponse(Long id, String name) implements DataResponse {


}
