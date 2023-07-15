package io.wisoft.wasabi.domain.auth.exception;

public class AuthExceptionExecutor {

    public static PasswordInvalidException passwordInvalid() {
        return new PasswordInvalidException();
    }
    public static LoginFailException loginFail() {
        return new LoginFailException();
    }
}
