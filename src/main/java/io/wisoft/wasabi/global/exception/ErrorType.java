package io.wisoft.wasabi.global.exception;


import org.springframework.http.HttpStatus;

public enum ErrorType {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-0001", "User Not Found"),
    MEMBER_EMAIL_OVERLAP(HttpStatus.CONFLICT, "USER-0003", "User Email Overlap"),
    DTO_INVALID(HttpStatus.BAD_REQUEST, "DTO-0001", "DTO Validate Fail"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "Auth-0001", "Login Fail"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-0002", "Token Not Exist"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-0003", "Member Not Activated"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-0001", "Board Not Found"),
    UNCAUGHT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-0000", "Uncaught Error Occur");


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
