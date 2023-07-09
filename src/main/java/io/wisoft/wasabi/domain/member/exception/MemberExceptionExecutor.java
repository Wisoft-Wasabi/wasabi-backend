package io.wisoft.wasabi.domain.member.exception;

import java.util.function.Supplier;

public class MemberExceptionExecutor {
    public static Supplier<MemberNotFoundException> MemberNotFound() {
        return MemberNotFoundException::new;
    }
}
