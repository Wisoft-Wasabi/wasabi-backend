package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class EmailOverlapException extends BusinessException {
    public EmailOverlapException() {
        super(ResponseType.MEMBER_EMAIL_OVERLAP);
    }

    public ResponseType getErrorType() {
        return ResponseType.MEMBER_EMAIL_OVERLAP;
    }
}
