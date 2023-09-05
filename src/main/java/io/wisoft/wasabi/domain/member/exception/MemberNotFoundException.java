package io.wisoft.wasabi.domain.member.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.config.web.response.ResponseType;


public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ResponseType.MEMBER_NOT_FOUND);
    }

    public ResponseType getErrorType() {
        return ResponseType.MEMBER_NOT_FOUND;
    }
}
