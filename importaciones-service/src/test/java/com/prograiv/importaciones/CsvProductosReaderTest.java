package com.prograiv.importaciones;

import com.prograiv.importaciones.service.CsvProductosReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class CsvProductosReaderTest {
    private final CsvProductosReader lector = new CsvProductosReader();
    private static final String CABECERA = "codigo,nombre,categoria,precio,existencia\n";

    private MockMultipartFile archivo(String contenido) {
        return new MockMultipartFile("archivo", "prueba.csv", "text/csv",
                contenido.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void admiteBomComasComillasYAcentosSinRomperLaCorrelacion() {
        var resultado = lector.leer(archivo("\uFEFF" + CABECERA
                + "P1,\"Ratón, modelo \"\"A\"\"\",Perifericos,10.50,0\n"));
        assertEquals(1, resultado.total());
        assertTrue(resultado.errores().isEmpty());
        assertEquals("2", resultado.validos().getFirst().itemId());
        assertEquals("Ratón, modelo \"A\"", resultado.validos().getFirst().nombre());
        assertEquals(0, resultado.validos().getFirst().existencia());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ",Nombre,Perifericos,10,1",
            "P1,,Perifericos,10,1",
            "P1,Nombre,,10,1",
            "P1,Nombre,Perifericos,0,1",
            "P1,Nombre,Perifericos,-1,1",
            "P1,Nombre,Perifericos,10.001,1",
            "P1,Nombre,Perifericos,10,-1",
            "P1,Nombre,Perifericos,10,1.5",
            "P1,Nombre,Perifericos,10,2147483648",
            "P1,Nombre,Perifericos,10",
            "P1,Nombre,Perifericos,10,1,extra"
    })
    void conservaFilaYMotivoParaDatosInvalidos(String fila) {
        var resultado = lector.leer(archivo(CABECERA + fila + "\n"));
        assertEquals(1, resultado.total());
        assertTrue(resultado.validos().isEmpty());
        assertEquals(2, resultado.errores().getFirst().fila());
        assertFalse(resultado.errores().getFirst().motivo().isBlank());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "codigo;nombre;categoria;precio;existencia\n",
            "codigo,nombre,categoria,precio,existencia\n",
            "codigo,nombre,categoria,precio,existencia\nP1,\"sin cierre,Perifericos,10,1"})
    void rechazaArchivosQueNoPuedenProcesarse(String contenido) {
        var error = assertThrows(ResponseStatusException.class,
                () -> lector.leer(archivo(contenido)));
        assertEquals(400, error.getStatusCode().value());
    }

    @Test
    void rechazaBytesUtf8Invalidos() {
        var archivo = new MockMultipartFile("archivo", "prueba.csv", "text/csv",
                new byte[]{(byte) 0xFF, (byte) 0xFE});
        assertThrows(ResponseStatusException.class, () -> lector.leer(archivo));
    }

    @Test
    void unaFilaInvalidaNoDescartaUnaFilaValidaPosterior() {
        var resultado = lector.leer(archivo(CABECERA
                + "P1,Mal,Perifericos,0,1\n"
                + "P2,Bien,Perifericos,10,2\n"));
        assertEquals(2, resultado.total());
        assertEquals(1, resultado.errores().size());
        assertEquals("3", resultado.validos().getFirst().itemId());
    }
}
