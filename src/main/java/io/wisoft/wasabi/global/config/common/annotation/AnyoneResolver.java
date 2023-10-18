package io.wisoft.wasabi.global.config.common.annotation;

import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
public class AnyoneResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public AnyoneResolver(final JwtTokenProvider jwtTokenProvider, final AuthorizationExtractor extractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Anyone.class);
    }

    @Override
    public Long resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final boolean isAuthenticated = (boolean) request.getAttribute("isAuthenticated");

        if (!isAuthenticated) {
            final UUID sessionId = UUID.fromString(request.getSession().getId());
            final Long accessId = sessionId.getMostSignificantBits();
            return accessId;
        }

        final String token = extractor.extract(request, "Bearer");

        return jwtTokenProvider.decodeAccessToken(token);
    }
}
