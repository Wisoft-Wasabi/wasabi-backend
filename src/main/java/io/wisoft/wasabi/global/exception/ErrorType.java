package io.wisoft.wasabi.global.exception;


import org.springframework.http.HttpStatus;

public enum ErrorType {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-0001", "User Not Found"),
    USER_INVALID(HttpStatus.CONFLICT, "USER-0002", "User Invalid");

    private HttpStatus httpStatusCode;
    private String errorCode;
    private String errorMessage;

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    ErrorType(final HttpStatus httpStatusCode, final String errorCode, final String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
