package com.prograiv.importaciones.exception;

import org.springframework.http.HttpStatus;

public class ImportacionException extends RuntimeException {

    private final Long importacionId;
    private final HttpStatus estado;

    public ImportacionException(
            Long importacionId,
            HttpStatus estado,
            String mensaje) {

        super(mensaje);
        this.importacionId = importacionId;
        this.estado = estado;
    }

    public Long getImportacionId() {
        return importacionId;
    }

    public HttpStatus getEstado() {
        return estado;
    }
}