package com.prograiv.importaciones;

import com.prograiv.importaciones.client.CatalogoClient;
import com.prograiv.importaciones.dto.ContratoCatalogo.ProductoEntrada;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class CatalogoClientTest {
    private ProductoEntrada entrada(String fila, String codigo) {
        return new ProductoEntrada(fila, codigo, "Nombre", "Perifericos",
                new BigDecimal("10.00"), 1);
    }

    @Test
    void ausenciaDeInstanciasSeClasificaComoFalloDeComunicacion() {
        var builder = RestClient.builder().requestInterceptor((request, body, execution) -> {
            throw new IllegalStateException("No instances available for catalogo-service");
        });
        var cliente = new CatalogoClient(builder);
        var error = assertThrows(RestClientException.class,
                () -> cliente.registrar(List.of(entrada("2", "A"))));
        assertInstanceOf(IllegalStateException.class, error.getCause());
    }

    @Test
    void resultadosDesordenadosSeRelacionanPorItemIdYNoPorPosicion() {
        var builder = RestClient.builder();
        var servidor = MockRestServiceServer.bindTo(builder).build();
        servidor.expect(requestTo("http://catalogo-service/api/productos/lote"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                    {"total":2,"aceptados":1,"rechazados":1,"resultados":[
                    {"itemId":"7","aceptado":false,"productoId":null,"motivo":"Duplicado"},
                    {"itemId":"2","aceptado":true,"productoId":99,"motivo":null}]}
                    """, MediaType.APPLICATION_JSON));
        var resultados = new CatalogoClient(builder).registrar(
                List.of(entrada("2", "A"), entrada("7", "B")));
        assertEquals(7, resultados.getFirst().fila());
        assertEquals("B", resultados.getFirst().codigo());
        assertEquals(99L, resultados.get(1).productoId());
        servidor.verify();
    }

    @Test
    void respuestaIncompletaNoSePresentaComoLoteCompletado() {
        var builder = RestClient.builder();
        var servidor = MockRestServiceServer.bindTo(builder).build();
        servidor.expect(requestTo("http://catalogo-service/api/productos/lote"))
                .andRespond(withSuccess("""
                    {"total":1,"aceptados":1,"rechazados":0,"resultados":[]}
                    """, MediaType.APPLICATION_JSON));
        assertThrows(RestClientException.class,
                () -> new CatalogoClient(builder).registrar(List.of(entrada("2", "A"))));
        servidor.verify();
    }
}
