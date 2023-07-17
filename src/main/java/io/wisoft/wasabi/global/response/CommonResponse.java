package io.wisoft.wasabi.global.response;

import java.time.LocalDateTime;

public class CommonResponse<T> {
    private T dataResponse;
    private LocalDateTime timestamp;

    protected CommonResponse() {
    }

    public T getDataResponse() {
        return dataResponse;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public static <T> CommonResponse<T> newInstance(T dataResponse) {
        CommonResponse response = new CommonResponse();
        response.dataResponse = dataResponse;
        response.timestamp = LocalDateTime.now().withNano(0);
        return response;
    }
}