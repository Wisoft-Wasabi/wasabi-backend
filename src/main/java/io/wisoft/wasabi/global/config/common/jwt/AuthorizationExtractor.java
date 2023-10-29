package io.wisoft.wasabi.global.config.common.jwt;

import io.wisoft.wasabi.global.config.common.Const;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

import java.util.Enumeration;

public class AuthorizationExtractor {
    public static String extract(final HttpServletRequest request, final String type) {
        final Enumeration<String> headers = request.getHeaders(Const.AUTH_HEADER);

        while (headers.hasMoreElements()) {
            final String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
}
