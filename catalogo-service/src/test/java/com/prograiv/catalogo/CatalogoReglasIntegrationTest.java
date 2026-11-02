package com.prograiv.catalogo;

import com.prograiv.catalogo.dto.LoteProductosRequest;
import com.prograiv.catalogo.dto.ProductoLoteRequest;
import com.prograiv.catalogo.repository.ProductoRepository;
import com.prograiv.catalogo.service.LoteProductosService;
import com.prograiv.catalogo.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CatalogoReglasIntegrationTest {
    @Autowired LoteProductosService lotes;
    @Autowired ProductoRepository productos;
    @Autowired ProductoService consultas;
    @Autowired JdbcTemplate jdbc;

    @BeforeEach
    void prepararDatosAislados() {
        jdbc.update("DELETE FROM productos");
        jdbc.update("DELETE FROM categorias");
        jdbc.update("INSERT INTO categorias(nombre) VALUES (?)", "Perifericos");
        jdbc.update("INSERT INTO categorias(nombre) VALUES (?)", "Monitores");
    }

    private ProductoLoteRequest producto(String item, String codigo, String precio) {
        return new ProductoLoteRequest(item, codigo, "Teclado " + codigo,
                "Perifericos", new BigDecimal(precio), 0);
    }

    @Test
    void unRechazoNoRevierteLosAceptadosNiImpideContinuarElLote() {
        var respuesta = lotes.procesar(new LoteProductosRequest(List.of(
                producto("1", "ABC1", "10.50"),
                producto("2", "abc1", "20.00"),
                producto("3", "ABC2", "0"),
                producto("4", "ABC3", "30.00"))));
        assertEquals(4, respuesta.total());
        assertEquals(2, respuesta.aceptados());
        assertEquals(2, respuesta.rechazados());
        assertEquals(2, productos.count());
        assertTrue(respuesta.resultados().get(3).aceptado());
        assertNotNull(respuesta.resultados().get(3).productoId());
    }

    @Test
    void referenciasDuplicadasInvalidanElSobreAntesDeEscribir() {
        var error = assertThrows(ResponseStatusException.class,
                () -> lotes.procesar(new LoteProductosRequest(List.of(
                        producto("igual", "A", "10"),
                        producto("igual", "B", "10")))));
        assertEquals(400, error.getStatusCode().value());
        assertEquals(0, productos.count());
    }

    @Test
    void filtrosYPaginacionMantienenElTotalYUnaPaginaEstable() {
        lotes.procesar(new LoteProductosRequest(List.of(
                producto("1", "BUSCAR1", "10"),
                producto("2", "BUSCAR2", "20"),
                producto("3", "OTRO", "30"))));
        Long categoria = jdbc.queryForObject(
                "SELECT id FROM categorias WHERE nombre = ?", Long.class, "Perifericos");
        var pagina = productos.buscar("buscar", "TECLADO", categoria,
                PageRequest.of(1, 1, Sort.by("id")));
        assertEquals(2, pagina.getTotalElements());
        assertEquals(2, pagina.getTotalPages());
        assertEquals(1, pagina.getContent().size());
        assertEquals("BUSCAR2", pagina.getContent().getFirst().getCodigo());
    }

    @Test
    void unProductoInexistenteResponde404() {
        var error = assertThrows(ResponseStatusException.class,
                () -> consultas.obtenerPorId(999999L));
        assertEquals(404, error.getStatusCode().value());
    }
}
