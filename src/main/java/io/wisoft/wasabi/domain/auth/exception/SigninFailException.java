package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class SigninFailException extends RuntimeException{

    private final ErrorType errorType;

    public SigninFailException() {
        errorType = ErrorType.LOGIN_FAIL;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
