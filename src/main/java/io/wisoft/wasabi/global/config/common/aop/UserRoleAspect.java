package io.wisoft.wasabi.global.config.common.aop;

import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class UserRoleAspect {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public UserRoleAspect(final JwtTokenProvider jwtTokenProvider, final AuthorizationExtractor extractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Around("@annotation(userRole)")
    public Object checkUserRole(ProceedingJoinPoint joinPoint, final UserRole userRole) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // TODO : 다른 Error 처리 방식 고안
        if(attributes == null) {
            throw new RuntimeException();
        }
        HttpServletRequest request = attributes.getRequest();

        final String token = extractor.extract(request, "Bearer");

        if (!StringUtils.hasText(token)) throw AuthExceptionExecutor.UnAuthorized();

        final Role currentRole = jwtTokenProvider.decodeRole(token);

        final Role requiredRole = userRole.value();

        if (!currentRole.equals(requiredRole)) {
            throw AuthExceptionExecutor.Forbidden();
        }

        return joinPoint.proceed();
    }
}
