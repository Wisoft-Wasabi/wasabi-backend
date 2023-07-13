package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class MemberNotFoundException extends RuntimeException {
    private final ErrorType errorType;

    public MemberNotFoundException() {
        this.errorType = ErrorType.MEMBER_NOT_FOUND;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
