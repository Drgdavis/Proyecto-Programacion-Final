package com.prograiv.catalogo.dto;

import java.util.List;

public class PaginaResponse<T> {

    private final List<T> contenido;
    private final int pagina;
    private final int tamanio;
    private final long totalElementos;
    private final int totalPaginas;

    public PaginaResponse(List<T> contenido, int pagina, int tamanio,
                          long totalElementos, int totalPaginas) {
        this.contenido = contenido;
        this.pagina = pagina;
        this.tamanio = tamanio;
        this.totalElementos = totalElementos;
        this.totalPaginas = totalPaginas;
    }

    public List<T> getContenido() {
        return contenido;
    }

    public int getPagina() {
        return pagina;
    }

    public int getTamanio() {
        return tamanio;
    }

    public long getTotalElementos() {
        return totalElementos;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }
}
