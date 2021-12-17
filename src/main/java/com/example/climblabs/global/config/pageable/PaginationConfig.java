package com.example.climblabs.global.config.pageable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PaginationConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        // page가 1부터 시작
        return p -> p.setOneIndexedParameters(true);
    }
}
