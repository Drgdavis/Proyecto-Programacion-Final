package com.prograiv.catalogo.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record LoteProductosRequest(

        @NotEmpty(message = "Debes enviar al menos un producto.")
        List<ProductoLoteRequest> productos

) {
}