package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class MemberEmailOverlapException extends RuntimeException {
    private final ErrorType errorType;

    public MemberEmailOverlapException() {
        this.errorType = ErrorType.MEMBER_EMAIL_OVERLAP;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
