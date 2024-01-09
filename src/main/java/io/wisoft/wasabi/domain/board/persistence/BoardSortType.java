package io.wisoft.wasabi.domain.board.persistence;

public enum BoardSortType {
    LATEST("latest"),
    VIEWS("views"),
    LIKES("likes"),
    DEFAULT("default");

    private final String sortType;

    BoardSortType(final String sortType) {
        this.sortType = sortType;
    }

    public final String getSortType() {
        return sortType;
    }
}

