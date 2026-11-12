package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.config.Cors;
import com.agrihusac.SysInvent.config.CorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CorsConfigTest {

    @Test
    void configuraMultiplesOrigenesSinEspaciosNiDuplicados() {
        Cors cors = new Cors();
        cors.setAllowOrigins(
                "http://localhost:5173, http://localhost:3000, http://localhost:5173"
        );
        cors.setAllowMethods("GET, POST, OPTIONS");
        cors.setAllowHeaders("*");
        cors.setExposedHeaders("Authorization");
        cors.setMaxAge(3600);
        cors.setAllowCredentials(true);
        cors.setMapping("/**");

        CorsConfigurationSource source =
                new CorsConfig().corsConfigurationSource(cors);

        MockHttpServletRequest request =
                new MockHttpServletRequest("OPTIONS", "/api/roles");

        CorsConfiguration configuration =
                source.getCorsConfiguration(request);

        assertNotNull(configuration);
        assertEquals(
                List.of("http://localhost:5173", "http://localhost:3000"),
                configuration.getAllowedOrigins()
        );
        assertEquals(
                List.of("GET", "POST", "OPTIONS"),
                configuration.getAllowedMethods()
        );
    }
}