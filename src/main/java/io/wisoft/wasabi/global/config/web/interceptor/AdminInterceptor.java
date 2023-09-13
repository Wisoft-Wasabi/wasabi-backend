package io.wisoft.wasabi.global.config.web.interceptor;

import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    public AdminInterceptor(
            final AuthorizationExtractor authExtractor,
            final JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws UnknownHostException {

        final String accessToken = authExtractor.extract(request, "Bearer");
        final String ip = getClientIp(request);

        if (!StringUtils.hasText(accessToken)) {
            logger.warn("[Request] token is not exist - path: {}, ip: {}", request.getRequestURI(), ip);
            throw AuthExceptionExecutor.UnAuthorized();
        }

        MDC.put("traceId", UUID.randomUUID().toString());

        final Role role = jwtTokenProvider.decodeRole(accessToken);
        if (role != Role.ADMIN) {
            logger.warn("[Request] invalid admin access - path: {}, ip: {}", request.getRequestURI(), ip);
            throw AuthExceptionExecutor.Forbidden();
        }

        return true;
    }

    private String getClientIp(final HttpServletRequest request) throws UnknownHostException {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
            final InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostName() + "/" + address.getHostAddress();
        }

        return ip;
    }
}
