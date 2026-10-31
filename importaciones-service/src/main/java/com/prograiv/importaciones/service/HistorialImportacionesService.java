package com.prograiv.importaciones.service;

import com.prograiv.importaciones.dto.*;
import com.prograiv.importaciones.repository.ImportacionRepository;
import com.prograiv.importaciones.repository.ResultadoImportacionRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class HistorialImportacionesService {

    private final ImportacionRepository importacionRepository;
    private final ResultadoImportacionRepository resultadoRepository;

    public HistorialImportacionesService(
            ImportacionRepository importacionRepository,
            ResultadoImportacionRepository resultadoRepository) {

        this.importacionRepository = importacionRepository;
        this.resultadoRepository = resultadoRepository;
    }

    public PaginaResponse<ImportacionResponse> listar(
            int pagina,
            int tamanio) {

        if (pagina < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La pagina debe ser igual o mayor que cero.");
        }

        if (tamanio < 1 || tamanio > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tamanio debe estar entre 1 y 100.");
        }

        var pageable = PageRequest.of(
                pagina,
                tamanio,
                Sort.by(Sort.Direction.DESC, "id")
        );

        var resultado = importacionRepository.findAll(pageable);

        var contenido = resultado.getContent()
                .stream()
                .map(ImportacionResponse::desde)
                .toList();

        return new PaginaResponse<>(
                contenido,
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages()
        );
    }

    public ImportacionDetalleResponse obtener(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El identificador debe ser mayor que cero.");
        }

        var importacion = importacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La importacion indicada no existe."));

        var resultados = resultadoRepository
                .findByImportacion_IdOrderByFilaAsc(id)
                .stream()
                .map(ResultadoFilaResponse::desde)
                .toList();

        return new ImportacionDetalleResponse(
                ImportacionResponse.desde(importacion),
                resultados
        );
    }
}