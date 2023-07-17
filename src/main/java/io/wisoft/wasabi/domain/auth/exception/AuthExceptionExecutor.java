package io.wisoft.wasabi.domain.auth.exception;

public class AuthExceptionExecutor {

    public static PasswordInvalidException PasswordInvalid() {
        return new PasswordInvalidException();
    }
    public static LoginFailException LoginFail() {
        return new LoginFailException();
    }


}
