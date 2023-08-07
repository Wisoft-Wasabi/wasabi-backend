package io.wisoft.wasabi.domain.member;

public enum Part {
    BACKEND("backend"), FRONTEND("frontend"), UNDEFINED("undefined");

    private final String partType;

    Part(String partType) {
        this.partType = partType;
    }

    final String getPartType() {
        return partType;
    }
}
