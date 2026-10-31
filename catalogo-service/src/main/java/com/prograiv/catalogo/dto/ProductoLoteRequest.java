package com.prograiv.catalogo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoLoteRequest(

        @NotBlank(message = "El identificador del elemento es obligatorio.")
        String itemId,

        @NotBlank(message = "El codigo es obligatorio.")
        @Size(max = 50, message = "El codigo admite hasta 50 caracteres.")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 150, message = "El nombre admite hasta 150 caracteres.")
        String nombre,

        @NotBlank(message = "La categoria es obligatoria.")
        @Size(max = 100, message = "La categoria admite hasta 100 caracteres.")
        String categoria,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(
                value = "0",
                inclusive = false,
                message = "El precio debe ser mayor que cero."
        )
        @Digits(
                integer = 10,
                fraction = 2,
                message = "El precio admite hasta 10 enteros y 2 decimales."
        )
        BigDecimal precio,

        @NotNull(message = "La existencia es obligatoria.")
        @Min(value = 0, message = "La existencia no puede ser negativa.")
        Integer existencia

) {
}