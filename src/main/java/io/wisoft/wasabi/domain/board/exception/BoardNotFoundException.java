package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class BoardNotFoundException extends BusinessException {

    public BoardNotFoundException() {
        super(ErrorType.BOARD_NOT_FOUND);
    }
}
