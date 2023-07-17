package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class PasswordInvalidException extends BusinessException {
    public PasswordInvalidException() {
        super(ErrorType.DTO_INVALID);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DTO_INVALID;
    }
}
