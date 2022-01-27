package com.example.climblabs.global.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/search/*")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST")
            .maxAge(3000);

        registry.addMapping("/posts/*")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST")
            .maxAge(3000);
    }
}
