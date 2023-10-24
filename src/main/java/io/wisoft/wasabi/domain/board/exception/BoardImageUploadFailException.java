package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class BoardImageUploadFailException extends BusinessException {

    public BoardImageUploadFailException() {
        super(ResponseType.BOARD_IMAGE_UPLOAD_FAIL);
    }
}
