package com.prograiv.catalogo.dto;

public class CategoriaResponse {

    private final Long id;
    private final String nombre;

    public CategoriaResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
