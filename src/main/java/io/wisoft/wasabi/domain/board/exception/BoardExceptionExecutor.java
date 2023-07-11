package io.wisoft.wasabi.domain.board.exception;

import io.wisoft.wasabi.domain.auth.exception.PasswordInvalidException;

public class BoardExceptionExecutor {

    public static BoardNotFoundException BoardNotFound() {
        return new BoardNotFoundException();
    }
}
