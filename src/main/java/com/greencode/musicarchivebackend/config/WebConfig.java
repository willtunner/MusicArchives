package com.greencode.musicarchivebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Permitir solicitações de qualquer origem
                .allowedMethods("*") // Permitir solicitações de qualquer método (GET, POST, etc.)
                .allowedHeaders("*"); // Permitir solicitações com qualquer cabeçalho
    }
}
