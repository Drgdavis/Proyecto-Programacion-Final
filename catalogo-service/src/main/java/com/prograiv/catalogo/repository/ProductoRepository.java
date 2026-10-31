package com.prograiv.catalogo.repository;

import com.prograiv.catalogo.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    String FILTROS = " WHERE (:codigo IS NULL OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :codigo, '%')))"
            + " AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))"
            + " AND (:categoriaId IS NULL OR c.id = :categoriaId)";

    @Query(value = "SELECT p FROM Producto p JOIN FETCH p.categoria c" + FILTROS,
            countQuery = "SELECT COUNT(p) FROM Producto p JOIN p.categoria c" + FILTROS)
    Page<Producto> buscar(@Param("codigo") String codigo,
                          @Param("nombre") String nombre,
                          @Param("categoriaId") Long categoriaId,
                          Pageable pageable);
}
