package io.wisoft.wasabi.global.config.common;

public final class Const {

    /**
     * AUTH
     */
    public static final String TOKEN_TYPE = "Bearer";
    public static final String IS_AUTHENTICATED = "isAuthenticated";
    public static final String AUTH_HEADER = "Authorization";

    /**
     * MDC 관련
     */
    public static final String TRACE_ID = "traceId";

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

    /**
     * 파일 확장자 검증에 사용
     */
    public static final String IMAGE_EXTENSION_JPEG = "image/jpeg";
    public static final String IMAGE_EXTENSION_PNG = "image/png";
    public static final String IMAGE_EXTENSION_JPG = "image/jpg";
}
