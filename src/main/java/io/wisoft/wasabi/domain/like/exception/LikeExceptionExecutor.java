package io.wisoft.wasabi.domain.like.exception;

public class LikeExceptionExecutor {

    public static LikeNotFoundException LikeNotFound() {
        return new LikeNotFoundException();
    }

}
