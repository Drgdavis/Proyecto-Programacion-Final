package com.prograiv.importaciones.client;

import com.prograiv.importaciones.dto.ContratoCatalogo.*;
import com.prograiv.importaciones.dto.ResultadoFilaResponse;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class CatalogoClient {

    private final RestClient cliente;

    public CatalogoClient(@LoadBalanced RestClient.Builder builder) {
        this.cliente = builder.build();
    }

    public List<ResultadoFilaResponse> registrar(
            List<ProductoEntrada> productos) {

        if (productos.isEmpty()) {
            return List.of();
        }

        LoteResponse respuesta = cliente.post()
                .uri("http://catalogo-service/api/productos/lote")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoteRequest(productos))
                .retrieve()
                .body(LoteResponse.class);

        if (respuesta == null
                || respuesta.resultados() == null
                || respuesta.total() != productos.size()
                || respuesta.resultados().size() != productos.size()) {
            throw new RestClientException(
                    "Respuesta incompleta del catalogo.");
        }

        var enviados = new HashMap<String, ProductoEntrada>();

        for (var producto : productos) {
            enviados.put(producto.itemId(), producto);
        }

        var recibidos = new HashSet<String>();
        var resultados = new ArrayList<ResultadoFilaResponse>();
        int aceptados = 0;

        for (var resultado : respuesta.resultados()) {
            if (resultado == null) {
                throw new RestClientException(
                        "El catalogo devolvio un resultado nulo.");
            }

            var producto = enviados.get(resultado.itemId());

            if (producto == null || !recibidos.add(resultado.itemId())) {
                throw new RestClientException(
                        "El catalogo devolvio referencias incorrectas.");
            }

            if (resultado.aceptado()) {
                if (resultado.productoId() == null
                        || resultado.productoId() <= 0) {
                    throw new RestClientException(
                            "Falta el identificador del producto aceptado.");
                }

                aceptados++;

            } else if (resultado.motivo() == null
                    || resultado.motivo().isBlank()) {
                throw new RestClientException(
                        "Falta el motivo de rechazo del producto.");
            }

            resultados.add(new ResultadoFilaResponse(
                    Integer.parseInt(resultado.itemId()),
                    producto.codigo(),
                    resultado.aceptado(),
                    resultado.productoId(),
                    resultado.motivo()
            ));
        }

        if (respuesta.aceptados() != aceptados
                || respuesta.rechazados() != productos.size() - aceptados) {
            throw new RestClientException(
                    "Los contadores del catalogo no coinciden.");
        }

        return resultados;
    }
}