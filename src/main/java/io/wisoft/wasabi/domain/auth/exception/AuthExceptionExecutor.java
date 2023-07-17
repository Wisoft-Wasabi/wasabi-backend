package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.domain.member.exception.EmailOverlapException;

public class AuthExceptionExecutor {

    public static PasswordInvalidException passwordInvalid() {
        return new PasswordInvalidException();
    }
    public static LoginFailException loginFail() {
        return new LoginFailException();
    }


}
