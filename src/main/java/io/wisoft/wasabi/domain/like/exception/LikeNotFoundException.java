package io.wisoft.wasabi.domain.like.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.config.web.response.ResponseType;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ResponseType.LIKE_NOT_FOUND);
    }

}
