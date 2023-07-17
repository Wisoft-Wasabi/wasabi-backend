package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class MemberEmailOverlapException extends BusinessException {
    public MemberEmailOverlapException() {
        super(ErrorType.MEMBER_EMAIL_OVERLAP.getErrorMessage());
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.MEMBER_EMAIL_OVERLAP;
    }
}
