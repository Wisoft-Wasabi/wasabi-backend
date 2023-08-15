package io.wisoft.wasabi.global.config.common.aop;

import io.wisoft.wasabi.domain.member.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserRole {
    Role value() default Role.GENERAL;
}
