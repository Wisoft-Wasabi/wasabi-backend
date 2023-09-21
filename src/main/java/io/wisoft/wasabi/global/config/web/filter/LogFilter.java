package io.wisoft.wasabi.global.config.web.filter;

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

        MDC.put("traceId", UUID.randomUUID().toString());

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


    // TODO : 코드리뷰 & 디렉토리 구조 리팩토링 시 이 클래스 바깥으로 뺼 것
    private final class Const {
        /**
         * IP 추출시 필요한 관련 헤더들
         */
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
        public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
        public static final String X_REAL_IP = "X-Real-IP";
        public static final String X_REALIP = "X-RealIP";
        public static final String REMOTE_ADDR = "REMOTE_ADDR";
        public static final String UNKNOWN = "unknown";
        public static final String LOCALHOST = "127.0.0.1";
        public static final String ALL_IP = "0:0:0:0:0:0:0:1";
    }
}
