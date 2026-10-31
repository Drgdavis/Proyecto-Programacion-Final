package com.prograiv.catalogo.service;

import com.prograiv.catalogo.dto.LoteProductosRequest;
import com.prograiv.catalogo.dto.LoteProductosResponse;
import com.prograiv.catalogo.dto.ProductoLoteRequest;
import com.prograiv.catalogo.dto.ResultadoProductoLote;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class LoteProductosService {

    private final RegistroProductoService registroProductoService;
    private final Validator validator;

    public LoteProductosService(
            RegistroProductoService registroProductoService,
            Validator validator
    ) {
        this.registroProductoService = registroProductoService;
        this.validator = validator;
    }

    public LoteProductosResponse procesar(LoteProductosRequest solicitud) {

        if (solicitud == null
                || solicitud.productos() == null
                || solicitud.productos().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes enviar al menos un producto."
            );
        }

        List<ProductoLoteRequest> productos = solicitud.productos();
        Set<String> identificadores = new HashSet<>();

        // Revisar la estructura completa antes de guardar productos.
        for (ProductoLoteRequest producto : productos) {

            if (producto == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El lote no puede contener elementos nulos."
                );
            }

            String itemId = producto.itemId();

            if (itemId != null
                    && !itemId.isBlank()
                    && !identificadores.add(itemId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cada itemId debe ser unico dentro del lote."
                );
            }
        }

        List<ResultadoProductoLote> resultados = new ArrayList<>();
        int aceptados = 0;

        for (ProductoLoteRequest producto : productos) {

            Set<ConstraintViolation<ProductoLoteRequest>> errores =
                    validator.validate(producto);

            if (!errores.isEmpty()) {
                Set<String> mensajes = new TreeSet<>();

                for (ConstraintViolation<ProductoLoteRequest> error : errores) {
                    mensajes.add(error.getMessage());
                }

                resultados.add(new ResultadoProductoLote(
                        producto.itemId(),
                        false,
                        null,
                        String.join("; ", mensajes)
                ));

                continue;
            }

            try {
                Long productoId = registroProductoService.registrar(producto);

                resultados.add(new ResultadoProductoLote(
                        producto.itemId(),
                        true,
                        productoId,
                        null
                ));

                aceptados++;

            } catch (IllegalArgumentException exception) {

                resultados.add(new ResultadoProductoLote(
                        producto.itemId(),
                        false,
                        null,
                        exception.getMessage()
                ));

            } catch (DataIntegrityViolationException exception) {

                resultados.add(new ResultadoProductoLote(
                        producto.itemId(),
                        false,
                        null,
                        "El producto incumple una restriccion de la base de datos."
                ));
            }
        }

        int total = productos.size();

        return new LoteProductosResponse(
                total,
                aceptados,
                total - aceptados,
                resultados
        );
    }
}