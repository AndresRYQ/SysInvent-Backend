package com.agrihusac.SysInvent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(Cors cors) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(cors.isAllowCredentials());
        configuration.setAllowedOrigins(Arrays.asList(cors.getAllowOrigins().split(",")));
        configuration.setAllowedMethods(Arrays.asList(cors.getAllowMethods().split(",")));
        configuration.setAllowedHeaders(Arrays.asList(cors.getAllowHeaders().split(",")));
        configuration.setExposedHeaders(Arrays.asList(cors.getExposedHeaders().split(",")));
        configuration.setMaxAge(cors.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(cors.getMapping(), configuration);
        return source;
    }
}
