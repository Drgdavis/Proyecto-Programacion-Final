package com.prograiv.importaciones.repository;

import com.prograiv.importaciones.entity.ResultadoImportacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoImportacionRepository
        extends JpaRepository<ResultadoImportacion, Long> {

    List<ResultadoImportacion> findByImportacion_IdOrderByFilaAsc(
            Long importacionId);
}