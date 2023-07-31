package io.wisoft.wasabi.domain.board.exception;

public class BoardExceptionExecutor {

    public static BoardNotFoundException BoardNotFound() {
        return new BoardNotFoundException();
    }

    public static BoardSortTypeInvalidException BoardSortTypeInvalidException() { return new BoardSortTypeInvalidException(); }
}
