package io.wisoft.wasabi.global.config.web.interceptor;

import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        final String accessToken = AuthorizationExtractor.extract(request, Const.TOKEN_TYPE);

        if (!StringUtils.hasText(accessToken)) {
            throw AuthExceptionExecutor.UnAuthorized();
        }

        final Role role = jwtTokenProvider.decodeRole(accessToken);
        if (role != Role.ADMIN) {
            throw AuthExceptionExecutor.Forbidden();
        }

        return true;
    }
}
