package io.wisoft.wasabi.global.response;


import io.wisoft.wasabi.global.response.dto.DataResponse;

import java.time.Instant;

public class CommonResponse {
        private DataResponse dataResponse;
        private Long timestamp;

    // TODO private? protected? 어떤걸 쓰는게 좋나?
    protected CommonResponse() {
    }

    public DataResponse getDataResponse() {
        return dataResponse;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public static CommonResponse newInstance(DataResponse dataResponse) {
        CommonResponse response = new CommonResponse();
        response.dataResponse = dataResponse;
        response.timestamp = Instant.now().getEpochSecond();
        return response;
    }


}
