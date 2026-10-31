package com.prograiv.importaciones.dto;

import java.util.List;

public record LecturaCsv(
        int total,
        List<ContratoCatalogo.ProductoEntrada> validos,
        List<ResultadoFilaResponse> errores) {
}