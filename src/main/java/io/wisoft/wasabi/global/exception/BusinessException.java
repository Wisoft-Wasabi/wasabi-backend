package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;

public abstract class BusinessException extends RuntimeException {
    private final ResponseType responseType;

    protected BusinessException(final ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseType getErrorType(){
        return responseType;
    }

}

