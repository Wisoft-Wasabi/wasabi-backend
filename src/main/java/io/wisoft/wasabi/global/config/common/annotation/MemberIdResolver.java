package io.wisoft.wasabi.global.config.common.annotation;

import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberIdResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public MemberIdResolver(final JwtTokenProvider jwtTokenProvider, final AuthorizationExtractor extractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = extractor.extract(request, "Bearer");

        if (!StringUtils.hasText(token)) {
            throw AuthExceptionExecutor.UnAuthorized();
        }

        return jwtTokenProvider.decodeAccessToken(token);
    }
}
