package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class PasswordInvalidException extends RuntimeException {
    private final ErrorType errorType;

    public PasswordInvalidException() {
        this.errorType = ErrorType.DTO_INVALID;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
