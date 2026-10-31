package com.prograiv.importaciones.controller;

import com.prograiv.importaciones.exception.ImportacionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ApiError(int estado, String mensaje) {
    }

    public record ImportacionError(
            int estado,
            String mensaje,
            Long importacionId) {
    }

    @ExceptionHandler(ImportacionException.class)
    public ResponseEntity<ImportacionError> manejarImportacion(
            ImportacionException exception) {

        return ResponseEntity.status(exception.getEstado())
                .body(new ImportacionError(
                        exception.getEstado().value(),
                        exception.getMessage(),
                        exception.getImportacionId()
                ));
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

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiError> manejarArchivoFaltante(
            MissingServletRequestPartException exception) {

        return ResponseEntity.badRequest().body(
                new ApiError(
                        400,
                        "Debes enviar el archivo en el campo archivo."
                )
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> manejarTamanio(
            MaxUploadSizeExceededException exception) {

        return ResponseEntity.status(413).body(
                new ApiError(
                        413,
                        "El archivo supera el limite de carga permitido."
                )
        );
    }
}