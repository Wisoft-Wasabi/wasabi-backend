package io.wisoft.wasabi.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    NOT_FOUND(BAD_REQUEST, "Common-NotFound-404", "해당 엔티티를 찾을 수 없습니다.");


    private HttpStatus httpStatusCode;
    private String errorCode;
    private String message;

    ErrorCode(final HttpStatus httpStatusCode, final String errorCode, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
