package io.wisoft.wasabi.global.exception;

public abstract class BusinessException extends RuntimeException {
    private final ErrorType errorType;

    protected BusinessException(final ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

}

