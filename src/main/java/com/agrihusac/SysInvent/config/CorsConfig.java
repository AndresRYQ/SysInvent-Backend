package com.agrihusac.SysInvent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(Cors cors) {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(cors.isAllowCredentials());
        configuration.setAllowedOrigins(splitAndTrim(cors.getAllowOrigins()));
        configuration.setAllowedMethods(splitAndTrim(cors.getAllowMethods()));
        configuration.setAllowedHeaders(splitAndTrim(cors.getAllowHeaders()));
        configuration.setExposedHeaders(splitAndTrim(cors.getExposedHeaders()));
        configuration.setMaxAge(cors.getMaxAge());

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(cors.getMapping(), configuration);
        return source;
    }

    private List<String> splitAndTrim(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .distinct()
                .toList();
    }
}