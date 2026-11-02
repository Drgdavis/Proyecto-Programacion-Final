package com.prograiv.gateway_service;

import com.prograiv.gateway.GatewayErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayErrorHandlerTest {

    private final GatewayErrorHandler manejador =
            new GatewayErrorHandler();

    @Test
    void servicioNoDisponibleConserva503YDevuelveJson() {
        var intercambio = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/productos").build()
        );

        var error = new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Detalle interno que no debe mostrarse"
        );

        manejador.handle(intercambio, error).block();

        assertThat(intercambio.getResponse().getStatusCode())
                .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

        assertThat(intercambio.getResponse().getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);

        String cuerpo = intercambio.getResponse()
                .getBodyAsString()
                .block();

        assertThat(cuerpo)
                .contains("\"estado\":503")
                .contains("No hay una instancia disponible")
                .doesNotContain("Detalle interno");
    }

    @Test
    void rutaInexistenteConserva404() {
        var intercambio = MockServerWebExchange.from(
                MockServerHttpRequest.get("/ruta-inexistente").build()
        );

        var error = new ResponseStatusException(
                HttpStatus.NOT_FOUND
        );

        manejador.handle(intercambio, error).block();

        assertThat(intercambio.getResponse().getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(intercambio.getResponse().getBodyAsString().block())
                .contains("\"estado\":404")
                .contains("La ruta solicitada no existe");
    }

    @Test
    void errorInesperadoDevuelve500SinExponerSuDetalle() {
        var intercambio = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/productos").build()
        );

        var error = new IllegalStateException(
                "Detalle interno del sistema"
        );

        manejador.handle(intercambio, error).block();

        assertThat(intercambio.getResponse().getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(intercambio.getResponse().getBodyAsString().block())
                .contains("\"estado\":500")
                .contains("No se pudo completar la solicitud")
                .doesNotContain("Detalle interno");
    }
}