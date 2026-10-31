package com.prograiv.catalogo.dto;

public class ApiError {

    private final int estado;
    private final String mensaje;

    public ApiError(int estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
