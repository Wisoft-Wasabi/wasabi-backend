package io.wisoft.wasabi.domain.like.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class ExistLikeException extends BusinessException {
    public ExistLikeException() {
        super(ResponseType.EXIST_LIKE);
    }
}
