package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.exception.BusinessException;

public class BoardSortTypeInvalidException extends BusinessException {
    public BoardSortTypeInvalidException() {
        super(ResponseType.SORT_TYPE_NOT_FOUND);
    }
}
