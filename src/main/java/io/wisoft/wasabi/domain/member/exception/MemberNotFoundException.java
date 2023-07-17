package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;


public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorType.MEMBER_NOT_FOUND);
    }

    public ErrorType getErrorType() {
        return ErrorType.MEMBER_NOT_FOUND;
    }
}
