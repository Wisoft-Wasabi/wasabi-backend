package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.config.web.response.ResponseType;

public class TokenNotExistException extends BusinessException {
    public TokenNotExistException() { super(ResponseType.UNAUTHORIZED); }
}
