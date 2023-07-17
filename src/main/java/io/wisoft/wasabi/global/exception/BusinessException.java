package io.wisoft.wasabi.global.exception;

public abstract class BusinessException extends RuntimeException {

    private final String message;

    public BusinessException(final String message) {
        super(message);
        this.message = message;
    }

    public abstract ErrorType getErrorType();

}

