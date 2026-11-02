package com.prograiv.importaciones.service;

import com.prograiv.importaciones.dto.*;
import com.prograiv.importaciones.dto.ContratoCatalogo.ProductoEntrada;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CsvProductosReader {

    private static final List<String> ENCABEZADOS = List.of(
            "codigo", "nombre", "categoria", "precio", "existencia"
    );

    public LecturaCsv leer(MultipartFile archivo) {
        String nombre = archivo.getOriginalFilename();

        if (nombre == null
                || !nombre.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            throw error("El archivo debe tener extension .csv.");
        }

        if (archivo.isEmpty()) {
            throw error("El archivo esta vacio.");
        }

        String contenido = decodificar(archivo);

        if (contenido.startsWith("\uFEFF")) {
            contenido = contenido.substring(1);
        }

        var formato = CSVFormat.RFC4180.builder()
                .setIgnoreEmptyLines(false)
                .setIgnoreSurroundingSpaces(true)
                .get();

        List<CSVRecord> registros;

        try (var parser = formato.parse(new StringReader(contenido))) {
            registros = parser.getRecords();
        } catch (IOException | UncheckedIOException | IllegalArgumentException exception) {
            throw error(
                    "El CSV tiene una estructura invalida. "
                            + "Revisa separadores y comillas.");
        }

        if (registros.isEmpty()) {
            throw error("El archivo no contiene encabezados.");
        }

        if (!registros.getFirst().toList().equals(ENCABEZADOS)) {
            throw error(
                    "Encabezados esperados: "
                            + "codigo,nombre,categoria,precio,existencia");
        }

        if (registros.size() == 1) {
            throw error("El archivo contiene encabezados pero no productos.");
        }

        var validos = new ArrayList<ProductoEntrada>();
        var errores = new ArrayList<ResultadoFilaResponse>();

        for (int indice = 1; indice < registros.size(); indice++) {
            CSVRecord registro = registros.get(indice);
            int fila = indice + 1;

            String codigo = registro.size() > 0
                    ? registro.get(0).trim()
                    : "";

            try {
                if (registro.size() != 5) {
                    throw new IllegalArgumentException(
                            "La fila debe contener exactamente 5 columnas.");
                }

                String nombreProducto = registro.get(1).trim();
                String categoria = registro.get(2).trim();
                String precioTexto = registro.get(3).trim();
                String existenciaTexto = registro.get(4).trim();

                validarTexto(codigo, "El codigo", 50);
                validarTexto(nombreProducto, "El nombre", 150);
                validarTexto(categoria, "La categoria", 100);

                if (!precioTexto.matches("[+-]?[0-9]+(\\.[0-9]+)?")) {
                    throw new IllegalArgumentException(
                            "El precio debe ser decimal y usar punto.");
                }

                BigDecimal precio = new BigDecimal(precioTexto);

                if (precio.signum() <= 0) {
                    throw new IllegalArgumentException(
                            "El precio debe ser mayor que cero.");
                }

                if (precio.scale() > 2
                        || precio.precision() - precio.scale() > 10) {
                    throw new IllegalArgumentException(
                            "El precio admite hasta 10 enteros y 2 decimales.");
                }

                if (!existenciaTexto.matches("[+-]?[0-9]+")) {
                    throw new IllegalArgumentException(
                            "La existencia debe ser un numero entero.");
                }

                int existencia;

                try {
                    existencia = Integer.parseInt(existenciaTexto);
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException(
                            "La existencia supera el rango permitido.");
                }

                if (existencia < 0) {
                    throw new IllegalArgumentException(
                            "La existencia no puede ser negativa.");
                }

                validos.add(new ProductoEntrada(
                        Integer.toString(fila),
                        codigo,
                        nombreProducto,
                        categoria,
                        precio,
                        existencia
                ));

            } catch (IllegalArgumentException exception) {
                String codigoResumen = codigo.length() > 255
                        ? codigo.substring(0, 255)
                        : codigo;

                errores.add(new ResultadoFilaResponse(
                        fila,
                        codigoResumen,
                        false,
                        null,
                        exception.getMessage()
                ));
            }
        }

        return new LecturaCsv(
                registros.size() - 1,
                List.copyOf(validos),
                List.copyOf(errores)
        );
    }

    private String decodificar(MultipartFile archivo) {
        try {
            var decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);

            return decoder.decode(
                    ByteBuffer.wrap(archivo.getBytes())
            ).toString();

        } catch (IOException exception) {
            throw error("No se pudo leer el archivo como UTF-8.");
        }
    }

    private void validarTexto(
            String valor,
            String campo,
            int longitudMaxima) {

        if (valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " es obligatorio.");
        }

        if (valor.length() > longitudMaxima) {
            throw new IllegalArgumentException(
                    campo + " supera " + longitudMaxima + " caracteres.");
        }
    }

    private ResponseStatusException error(String mensaje) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST, mensaje);
    }
}