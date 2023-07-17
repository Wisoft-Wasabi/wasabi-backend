package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class EmailOverlapException extends BusinessException {
    public EmailOverlapException() {
        super(ErrorType.MEMBER_EMAIL_OVERLAP.getErrorMessage());
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.MEMBER_EMAIL_OVERLAP;
    }
}
