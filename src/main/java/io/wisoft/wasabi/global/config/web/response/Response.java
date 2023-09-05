package io.wisoft.wasabi.global.config.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class Response<T> {

    private final String code;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    @JsonIgnore
    private final HttpStatus status;

    private Response(
            final ResponseType responseType,
            final T data
    ) {
        this.code = responseType.getCode();
        this.message = responseType.getMessage();
        this.data = data;
        this.timestamp = LocalDateTime.now().withNano(0);

        this.status = responseType.getStatus();
    }

    public static <T> Response<T> of(
            final ResponseType responseType,
            final T data
    ) {
        return new Response<>(
                responseType,
                data
        );
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}