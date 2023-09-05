package io.wisoft.wasabi.domain.auth.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.config.web.response.ResponseType;

public class NotActivatedException extends BusinessException {
    public NotActivatedException() { super(ResponseType.FORBIDDEN); }

}
