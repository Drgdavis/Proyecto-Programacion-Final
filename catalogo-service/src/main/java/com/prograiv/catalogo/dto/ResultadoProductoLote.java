package com.prograiv.catalogo.dto;

public record ResultadoProductoLote(
        String itemId,
        boolean aceptado,
        Long productoId,
        String motivo
) {
}