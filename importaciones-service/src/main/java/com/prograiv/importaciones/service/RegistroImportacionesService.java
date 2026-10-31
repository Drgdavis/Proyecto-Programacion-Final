package com.prograiv.importaciones.service;

import com.prograiv.importaciones.dto.ResultadoFilaResponse;
import com.prograiv.importaciones.entity.Importacion;
import com.prograiv.importaciones.entity.ResultadoImportacion;
import com.prograiv.importaciones.repository.ImportacionRepository;
import com.prograiv.importaciones.repository.ResultadoImportacionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistroImportacionesService {

    private final ImportacionRepository importacionRepository;
    private final ResultadoImportacionRepository resultadoRepository;

    public RegistroImportacionesService(
            ImportacionRepository importacionRepository,
            ResultadoImportacionRepository resultadoRepository) {

        this.importacionRepository = importacionRepository;
        this.resultadoRepository = resultadoRepository;
    }

    @Transactional
    public Long iniciar(String archivo) {
        var importacion = new Importacion(archivo);

        return importacionRepository.saveAndFlush(importacion).getId();
    }

    @Transactional
    public void preparar(
            Long id,
            int total,
            List<ResultadoFilaResponse> erroresLocales) {

        var importacion = buscar(id);
        importacion.preparar(total, erroresLocales.size());
        guardarResultados(importacion, erroresLocales);
    }

    @Transactional
    public void finalizar(
            Long id,
            List<ResultadoFilaResponse> resultadosCatalogo) {

        var importacion = buscar(id);

        int aceptados = (int) resultadosCatalogo.stream()
                .filter(ResultadoFilaResponse::aceptado)
                .count();

        int rechazadosCatalogo = resultadosCatalogo.size() - aceptados;

        importacion.completar(
                aceptados,
                importacion.getRechazados() + rechazadosCatalogo
        );

        guardarResultados(importacion, resultadosCatalogo);
    }

    @Transactional
    public void fallar(Long id, String mensaje) {
        buscar(id).marcarFallida(mensaje);
    }

    private Importacion buscar(Long id) {
        return importacionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el historial de la importacion."));
    }

    private void guardarResultados(
            Importacion importacion,
            List<ResultadoFilaResponse> resultados) {

        var entidades = resultados.stream()
                .map(resultado -> new ResultadoImportacion(
                        importacion,
                        resultado.fila(),
                        resultado.codigo(),
                        resultado.aceptado(),
                        resultado.productoId(),
                        resultado.motivo()
                ))
                .toList();

        resultadoRepository.saveAll(entidades);
    }
}