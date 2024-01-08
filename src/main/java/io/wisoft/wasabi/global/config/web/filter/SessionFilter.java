package io.wisoft.wasabi.global.config.web.filter;

import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String token = AuthorizationExtractor.extract(request, Const.TOKEN_TYPE);

        final boolean isAuthenticated = StringUtils.hasText(token);
        request.setAttribute(Const.IS_AUTHENTICATED, isAuthenticated);

        if (!isAuthenticated) {
            request.getSession();

        }

        doFilter(request, response, filterChain);
    }

}
