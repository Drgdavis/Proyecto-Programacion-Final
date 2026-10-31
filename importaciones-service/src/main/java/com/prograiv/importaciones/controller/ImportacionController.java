package com.prograiv.importaciones.controller;

import com.prograiv.importaciones.dto.*;
import com.prograiv.importaciones.service.HistorialImportacionesService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/importaciones")
public class ImportacionController {

    private final HistorialImportacionesService historialService;

    public ImportacionController(
            HistorialImportacionesService historialService) {

        this.historialService = historialService;
    }

    @GetMapping
    public PaginaResponse<ImportacionResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return historialService.listar(page, size);
    }

    @GetMapping("/{id}")
    public ImportacionDetalleResponse obtener(
            @PathVariable Long id) {

        return historialService.obtener(id);
    }
}