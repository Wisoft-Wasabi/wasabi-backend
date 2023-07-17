package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class LoginFailException extends BusinessException{

    public LoginFailException() {
        super(ErrorType.LOGIN_FAIL.getErrorMessage());
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.LOGIN_FAIL;
    }
}
