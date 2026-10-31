package com.prograiv.catalogo.dto;

import java.math.BigDecimal;

public class ProductoResponse {

    private final Long id;
    private final String codigo;
    private final String nombre;
    private final Long categoriaId;
    private final String categoria;
    private final BigDecimal precio;
    private final Integer existencia;

    public ProductoResponse(Long id, String codigo, String nombre, Long categoriaId,
                            String categoria, BigDecimal precio, Integer existencia) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.categoria = categoria;
        this.precio = precio;
        this.existencia = existencia;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getExistencia() {
        return existencia;
    }
}
