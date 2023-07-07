package io.wisoft.wasabi.global.exception;

public class NotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundException() {
        this.errorCode = ErrorCode.NOT_FOUND;
    }

    public NotFoundException(final String message) {
        super(message);
        this.errorCode = ErrorCode.NOT_FOUND;
    }
}
