package io.wisoft.wasabi.global.response;


import io.wisoft.wasabi.global.response.dto.DataResponse;

import java.time.Instant;

public class CommonResponse<T> {
        private T dataResponse;
        private Instant timestamp;

    protected CommonResponse() {
    }

    public T getDataResponse() {
        return dataResponse;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public static CommonResponse newInstance(DataResponse dataResponse) {
        CommonResponse response = new CommonResponse();
        response.dataResponse = dataResponse;
        response.timestamp = Instant.now();
        return response;
    }
}
