package io.wisoft.wasabi.global.config.web.filter;

import io.wisoft.wasabi.global.config.common.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class LogFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        MDC.put(Const.TRACE_ID, UUID.randomUUID().toString());

        final String ip = getClientIp(request);
        logger.info("[Request] path: {}, ip: {}", request.getRequestURI(), ip);

        chain.doFilter(request, response);
    }

    /**
     * 각 환경, 프록시 서버, WAS마다 네이밍이 다르므로 직접 구해줘야한다.
     */
    private String getClientIp(final HttpServletRequest request) throws UnknownHostException {

        final String[] headers = {
                Const.X_FORWARDED_FOR,
                Const.PROXY_CLIENT_IP,
                Const.WL_PROXY_CLIENT_IP,
                Const.HTTP_CLIENT_IP,
                Const.HTTP_X_FORWARDED_FOR,
                Const.X_REAL_IP,
                Const.X_REALIP,
                Const.REMOTE_ADDR,
                Const.UNKNOWN,
                Const.LOCALHOST,
                Const.ALL_IP
        };

        String ip = Strings.EMPTY;

        for (final String header : headers) {
            if (!StringUtils.hasText(ip) || Const.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }

        if (!StringUtils.hasText(ip) || Const.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (Const.ALL_IP.equals(ip) || Const.LOCALHOST.equals(ip)) {
            final InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostName() + "/" + address.getHostAddress();
        }

        return ip;
    }
}
