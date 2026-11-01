package com.prograiv.importaciones.controller;

import com.prograiv.importaciones.dto.*;
import com.prograiv.importaciones.service.HistorialImportacionesService;
import com.prograiv.importaciones.service.ImportacionCsvService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/importaciones")
@Tag(
        name = "Importaciones",
        description = "Carga CSV, historial y resultados"
)
public class ImportacionController {

    private final HistorialImportacionesService historialService;
    private final ImportacionCsvService importacionService;

    public ImportacionController(
            HistorialImportacionesService historialService,
            ImportacionCsvService importacionService) {

        this.historialService = historialService;
        this.importacionService = importacionService;
    }

    @Operation(
            summary = "Importar productos desde un CSV",
            description = "Enviar archivo como multipart/form-data, "
                    + "en el campo archivo. CSV UTF-8 separado por comas. "
                    + "Encabezados: codigo,nombre,categoria,precio,existencia. "
                    + "Una importacion completada puede contener filas rechazadas. "
                    + "Los errores de archivo responden 400; "
                    + "los fallos al confirmar el catalogo responden 502."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Importacion procesada y resultado guardado"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacionDetalleResponse> importar(
            @RequestParam("archivo") MultipartFile archivo) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(importacionService.importar(archivo));
    }

    @Operation(
            summary = "Consultar el historial de importaciones",
            description = "Ordenado desde la importacion mas reciente. "
                    + "page comienza en 0; size admite valores entre 1 y 100."
    )
    @GetMapping
    public PaginaResponse<ImportacionResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return historialService.listar(page, size);
    }

    @Operation(
            summary = "Consultar el resultado de una importacion",
            description = "Incluye resumen y resultados disponibles por fila."
    )
    @GetMapping("/{id}")
    public ImportacionDetalleResponse obtener(
            @PathVariable Long id) {

        return historialService.obtener(id);
    }

    @Operation(
            summary = "Consultar las filas rechazadas de una importacion"
    )
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