package com.prograiv.importaciones.repository;

import com.prograiv.importaciones.entity.Importacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportacionRepository
        extends JpaRepository<Importacion, Long> {
}