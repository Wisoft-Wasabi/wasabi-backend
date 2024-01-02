package io.wisoft.wasabi.customization.container;

import io.wisoft.wasabi.domain.board.Board;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BoardStaticContainer {

    public static final Map<Integer, Board> BOARD_MAP = new ConcurrentHashMap<>();

    public static Optional<Board> get(final Integer key) {
        return Optional.ofNullable(BOARD_MAP.get(key));
    }
}
