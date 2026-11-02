package com.prograiv.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GatewayErrorHandler implements WebExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GatewayErrorHandler.class);

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            Throwable error) {

        var respuesta = exchange.getResponse();

        if (respuesta.isCommitted()) {
            return Mono.error(error);
        }

        int estado = 500;

        if (error instanceof ResponseStatusException exception) {
            estado = exception.getStatusCode().value();
            respuesta.getHeaders().addAll(exception.getHeaders());
        }

        String mensaje = switch (estado) {
            case 400 -> "La solicitud no es valida.";
            case 404 -> "La ruta solicitada no existe en el Gateway.";
            case 405 -> "El metodo HTTP no esta permitido.";
            case 503 -> "No hay una instancia disponible del servicio solicitado.";
            case 504 -> "El servicio tardo demasiado en responder.";
            default -> estado >= 500
                    ? "No se pudo completar la solicitud al servicio."
                    : "El Gateway rechazo la solicitud.";
        };

        String ruta = exchange.getRequest().getPath().value();

        if (estado >= 500) {
            log.error(
                    "Gateway: ruta={} estado={} tipo={}",
                    ruta,
                    estado,
                    error.getClass().getSimpleName()
            );
        } else {
            log.warn(
                    "Gateway: ruta={} estado={}",
                    ruta,
                    estado
            );
        }

        respuesta.setStatusCode(HttpStatusCode.valueOf(estado));
        respuesta.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String json = "{\"estado\":" + estado
                + ",\"mensaje\":\"" + mensaje + "\"}";

        byte[] contenido = json.getBytes(StandardCharsets.UTF_8);

        return respuesta.writeWith(
                Mono.just(
                        respuesta.bufferFactory().wrap(contenido)
                )
        );
    }
}