package io.wisoft.wasabi.global.exception;

import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.error.ErrorDataResponse;
import org.springframework.http.ResponseEntity;

public abstract class BusinessException extends RuntimeException {

    private final String message;

    public BusinessException(final String message) {
        super(message);
        this.message = message;
    }

    public abstract ErrorType getErrorType();

    protected abstract ResponseEntity<CommonResponse> buildResponse(ErrorType errorType);
}

