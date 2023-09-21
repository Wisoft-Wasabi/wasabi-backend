package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class LoginFailException extends BusinessException {
    public LoginFailException() { super(ResponseType.LOGIN_FAIL);}

}
