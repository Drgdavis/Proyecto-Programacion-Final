package com.prograiv.importaciones.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de importaciones",
                version = "1.0",
                description = "Carga de productos desde CSV UTF-8, "
                        + "historial y resultados por fila."
        )
)
public class OpenApiConfig {
}