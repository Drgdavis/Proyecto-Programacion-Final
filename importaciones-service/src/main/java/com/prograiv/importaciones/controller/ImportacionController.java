package com.prograiv.importaciones.controller;

import com.prograiv.importaciones.dto.*;
import com.prograiv.importaciones.service.HistorialImportacionesService;
import com.prograiv.importaciones.service.ImportacionCsvService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/importaciones")
public class ImportacionController {

    private final HistorialImportacionesService historialService;
    private final ImportacionCsvService importacionService;

    public ImportacionController(
            HistorialImportacionesService historialService,
            ImportacionCsvService importacionService) {

        this.historialService = historialService;
        this.importacionService = importacionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacionDetalleResponse> importar(
            @RequestParam("archivo") MultipartFile archivo) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(importacionService.importar(archivo));
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

    @GetMapping("/{id}/errores")
    public List<ResultadoFilaResponse> errores(
            @PathVariable Long id) {

        return historialService.obtener(id)
                .resultados()
                .stream()
                .filter(resultado -> !resultado.aceptado())
                .toList();
    }
}