package io.wisoft.wasabi.domain.like.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ResponseType.LIKE_NOT_FOUND);
    }

}
