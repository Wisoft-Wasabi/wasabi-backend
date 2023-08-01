package io.wisoft.wasabi.global.config.common.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Component
public class AuthorizationExtractor {
    public String extract(final HttpServletRequest request, final String type) {
        final Enumeration<String> headers = request.getHeaders("Authorization");

        while (headers.hasMoreElements()) {
            final String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
}
