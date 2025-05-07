package dev.vinpol.nebula.dragonship.web;

import dev.vinpol.nebula.dragonship.web.support.servlet.LocaleHeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LocaleHeaderInterceptor localeHeaderInterceptor;

    @Autowired
    public WebConfig(LocaleHeaderInterceptor localeHeaderInterceptor) {
        this.localeHeaderInterceptor = localeHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(localeHeaderInterceptor)
            .addPathPatterns("/**");  // apply to all requests
    }
}
