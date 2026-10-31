package com.prograiv.importaciones.dto;

import com.prograiv.importaciones.entity.ResultadoImportacion;

public record ResultadoFilaResponse(
        int fila,
        String codigo,
        boolean aceptado,
        Long productoId,
        String motivo) {

    public static ResultadoFilaResponse desde(
            ResultadoImportacion resultado) {

        return new ResultadoFilaResponse(
                resultado.getFila(),
                resultado.getCodigo(),
                resultado.isAceptado(),
                resultado.getProductoId(),
                resultado.getMotivo()
        );
    }
}