package io.wisoft.wasabi.global.config.web;

import io.wisoft.wasabi.global.config.common.annotation.AdminRoleResolver;
import io.wisoft.wasabi.global.config.common.annotation.AnyoneResolver;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableAspectJAutoProxy
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberIdResolver memberIdResolver;
    private final AnyoneResolver anyoneResolver;
    private final AdminRoleResolver adminRoleResolver;


    public WebMvcConfig(final MemberIdResolver memberIdResolver,
                        final AnyoneResolver anyoneResolver,
                        final AdminRoleResolver adminRoleResolver) {
        this.memberIdResolver = memberIdResolver;
        this.anyoneResolver = anyoneResolver;
        this.adminRoleResolver = adminRoleResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdResolver);
        resolvers.add(anyoneResolver);
        resolvers.add(adminRoleResolver);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
