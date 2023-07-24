package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class TokenNotExistException extends RuntimeException {

    private final ErrorType errorType;

    public TokenNotExistException() {
        this.errorType = ErrorType.UNAUTHORIZED;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
