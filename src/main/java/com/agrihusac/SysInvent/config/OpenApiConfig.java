package com.agrihusac.SysInvent.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SysInvent API",
                version = "v1",
                description = "Documentación de la API REST de SysInvent"
        )
)
public class OpenApiConfig {
}
