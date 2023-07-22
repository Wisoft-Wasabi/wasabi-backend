package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class TokenNotExistException extends BusinessException {

    public TokenNotExistException() {
        super(ErrorType.UNAUTHORIZED);
    }

}
