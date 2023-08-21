package io.wisoft.wasabi.global.config.web;

import io.wisoft.wasabi.global.config.common.annotation.AnyoneResolver;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberIdResolver memberIdResolver;
    private final AnyoneResolver anyoneResolver;

    public WebMvcConfig(final MemberIdResolver memberIdResolver,
                        final AnyoneResolver anyoneResolver) {
        this.memberIdResolver = memberIdResolver;
        this.anyoneResolver = anyoneResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdResolver);
        resolvers.add(anyoneResolver);
    }

    @Bean
    public FilterRegistrationBean<LogFilter> filterRegistrationBean() {
        return new FilterRegistrationBean<>(new LogFilter());
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
