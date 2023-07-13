package io.wisoft.wasabi.domain.auth.exception;

public class AuthExceptionExecutor {

    public static PasswordInvalidException PasswordInvalid() {
        return new PasswordInvalidException();
    }
    public static SigninFailException SigninFail() {
        return new SigninFailException();
    }

    public static TokenNotExistException UnAuthorized() {
        return new TokenNotExistException();
    }
}
