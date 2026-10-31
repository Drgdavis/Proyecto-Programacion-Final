package com.prograiv.importaciones.dto;

import java.math.BigDecimal;
import java.util.List;

public final class ContratoCatalogo {

    private ContratoCatalogo() {
    }

    public record ProductoEntrada(
            String itemId,
            String codigo,
            String nombre,
            String categoria,
            BigDecimal precio,
            Integer existencia) {
    }

    public record LoteRequest(List<ProductoEntrada> productos) {
    }

    public record ResultadoItem(
            String itemId,
            boolean aceptado,
            Long productoId,
            String motivo) {
    }

    public record LoteResponse(
            int total,
            int aceptados,
            int rechazados,
            List<ResultadoItem> resultados) {
    }
}