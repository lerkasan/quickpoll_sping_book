package ua.com.foxminded.lerkasan.quickpoll.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfigAdapter implements WebMvcConfigurer {

    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_START_PAGE = 0;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(DEFAULT_START_PAGE, DEFAULT_PAGE_SIZE));
        argumentResolvers.add(resolver);
    }
}
