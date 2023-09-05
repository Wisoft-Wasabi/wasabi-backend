package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.config.web.response.ResponseType;

public class LoginFailException extends BusinessException {
    public LoginFailException() { super(ResponseType.LOGIN_FAIL);}

}
