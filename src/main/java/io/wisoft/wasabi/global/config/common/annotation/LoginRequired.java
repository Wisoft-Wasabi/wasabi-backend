package io.wisoft.wasabi.global.config.common.annotation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@MemberId
@Valid @NotNull
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface LoginRequired {
}
