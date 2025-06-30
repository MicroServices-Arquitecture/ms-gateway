package com.project.gateway.gatewayms;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsGlobalConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
        .allowedOrigins("https://react-front-microservicios-production.up.railway.app")
        .allowedMethods("GET", "POST", "PUT", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}
