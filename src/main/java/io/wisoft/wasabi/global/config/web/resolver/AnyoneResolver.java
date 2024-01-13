package io.wisoft.wasabi.global.config.web.resolver;

import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
public class AnyoneResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AnyoneResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
        final boolean isAuthenticated = (boolean) request.getAttribute(Const.IS_AUTHENTICATED);

        if (!isAuthenticated) {
            final UUID sessionId = UUID.fromString(request.getSession().getId());
            final Long accessId = sessionId.getMostSignificantBits();
            return accessId;
        }

        final String token = AuthorizationExtractor.extract(request, Const.TOKEN_TYPE);

        return jwtTokenProvider.decodeAccessToken(token);
    }
}
