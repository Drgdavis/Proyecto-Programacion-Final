package com.prograiv.catalogo.exception;

import com.prograiv.catalogo.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> manejarEstado(ResponseStatusException exception) {
        int estado = exception.getStatusCode().value();
        String mensaje = exception.getReason();

        if (mensaje == null) {
            mensaje = "La solicitud no pudo procesarse.";
        }

        return ResponseEntity.status(estado).body(new ApiError(estado, mensaje));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoInvalido(MethodArgumentTypeMismatchException exception) {
        String mensaje = "El parametro " + exception.getName() + " tiene un formato invalido.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), mensaje));
    }
}
