package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.global.exception.BusinessException;
import io.wisoft.wasabi.global.exception.ErrorType;

public class BoardSortTypeInvalidException extends BusinessException {
    public BoardSortTypeInvalidException() {
        super(ErrorType.SORT_TYPE_NOT_FOUND);
    }
}
