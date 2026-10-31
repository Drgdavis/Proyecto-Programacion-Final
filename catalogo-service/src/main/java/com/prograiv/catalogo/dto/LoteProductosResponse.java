package com.prograiv.catalogo.dto;

import java.util.List;

public record LoteProductosResponse(
        int total,
        int aceptados,
        int rechazados,
        List<ResultadoProductoLote> resultados
) {
}