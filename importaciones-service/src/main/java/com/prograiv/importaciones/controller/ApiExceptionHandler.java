package com.prograiv.importaciones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ApiError(int estado, String mensaje) {
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> manejarEstado(
            ResponseStatusException exception) {

        String mensaje = exception.getReason() == null
                ? "No se pudo completar la solicitud."
                : exception.getReason();

        return ResponseEntity.status(exception.getStatusCode())
                .body(new ApiError(
                        exception.getStatusCode().value(),
                        mensaje
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipo(
            MethodArgumentTypeMismatchException exception) {

        return ResponseEntity.badRequest().body(
                new ApiError(
                        400,
                        "El parametro " + exception.getName()
                                + " tiene un formato incorrecto."
                )
        );
    }
}