package io.wisoft.wasabi.domain.auth.exception;

public class AuthExceptionExecutor {
    public static LoginFailException LoginFail() { return new LoginFailException(); }

    public static TokenNotExistException UnAuthorized() {
        return new TokenNotExistException();
    }

    public static PermissionDeniedException Forbidden() {
        return new PermissionDeniedException();
    }
}
