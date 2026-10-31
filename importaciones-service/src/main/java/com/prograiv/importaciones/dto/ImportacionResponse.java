package com.prograiv.importaciones.dto;

import com.prograiv.importaciones.entity.Importacion;
import java.time.LocalDateTime;

public record ImportacionResponse(
        Long id,
        String archivo,
        LocalDateTime fecha,
        String estado,
        int total,
        int aceptados,
        int rechazados) {

    public static ImportacionResponse desde(Importacion importacion) {
        return new ImportacionResponse(
                importacion.getId(),
                importacion.getArchivo(),
                importacion.getFecha(),
                importacion.getEstado(),
                importacion.getTotal(),
                importacion.getAceptados(),
                importacion.getRechazados()
        );
    }
}