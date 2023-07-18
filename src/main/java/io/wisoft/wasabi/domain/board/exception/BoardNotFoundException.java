package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.exception.ErrorType;

public class BoardNotFoundException extends RuntimeException {

    private final ErrorType errorType;

    public BoardNotFoundException() {
        this.errorType = ErrorType.BOARD_NOT_FOUND;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
