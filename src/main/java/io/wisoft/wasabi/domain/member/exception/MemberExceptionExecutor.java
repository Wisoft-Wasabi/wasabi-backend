package io.wisoft.wasabi.domain.member.exception;

public class MemberExceptionExecutor {
    public static MemberNotFoundException memberNotFound() {
        return new MemberNotFoundException();
    }
}
