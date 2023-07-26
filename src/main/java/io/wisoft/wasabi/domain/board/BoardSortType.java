package io.wisoft.wasabi.domain.board;

public enum BoardSortType {
    CREATED_AT("createdAt"),
    VIEWS("views"),
    LIKES("likes"),
    DEFAULT("default");

    private final String sortType;

    BoardSortType(String sortType) {
        this.sortType = sortType;
    }

    final String getSortType() {
        return sortType;
    }
}

