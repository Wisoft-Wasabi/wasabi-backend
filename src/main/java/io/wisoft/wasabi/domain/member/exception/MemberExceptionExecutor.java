package io.wisoft.wasabi.domain.member.exception;

public class MemberExceptionExecutor {

    public static MemberNotFoundException MemberNotFound() {
        return new MemberNotFoundException();
    }

    public static MemberEmailOverlapException MemberEmailOverlap() {
        return new MemberEmailOverlapException();
    }
}
