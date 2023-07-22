package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class NotActivatedException extends RuntimeException {

    public final ErrorType errorType;

    public NotActivatedException() {
        this.errorType = ErrorType.FORBIDDEN;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
