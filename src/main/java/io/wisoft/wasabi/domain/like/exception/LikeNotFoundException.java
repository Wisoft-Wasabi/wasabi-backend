package io.wisoft.wasabi.domain.like.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class LikeNotFoundException extends RuntimeException{
    private final ErrorType errorType;
    public LikeNotFoundException() {
        this.errorType = ErrorType.LIKE_NOT_FOUND;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
