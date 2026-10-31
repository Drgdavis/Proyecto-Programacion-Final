package com.prograiv.importaciones.dto;

import java.util.List;

public record ImportacionDetalleResponse(
        ImportacionResponse importacion,
        List<ResultadoFilaResponse> resultados) {
}