package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class BoardNotFoundException extends BusinessException {

    public BoardNotFoundException() {
        super(ResponseType.BOARD_NOT_FOUND);
    }
}
