package io.wisoft.wasabi.domain.like.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ErrorType.LIKE_NOT_FOUND);
    }

}
