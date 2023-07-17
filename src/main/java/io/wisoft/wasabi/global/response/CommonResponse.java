package io.wisoft.wasabi.global.response;

import java.time.LocalDateTime;

public class CommonResponse<T> {
    private T data;
    private LocalDateTime timestamp;

    private CommonResponse() {
    }

    public static <T> CommonResponse<T> newInstance(final T data) {
        final CommonResponse response = new CommonResponse();
        response.data = data;
        response.timestamp = LocalDateTime.now().withNano(0);
        return response;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}