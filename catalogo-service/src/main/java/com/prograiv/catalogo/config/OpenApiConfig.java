package com.prograiv.catalogo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de catalogo",
                version = "1.0",
                description = "Consulta de categorias y productos, "
                        + "filtros, paginacion y registro por lote."
        )
)
public class OpenApiConfig {
}