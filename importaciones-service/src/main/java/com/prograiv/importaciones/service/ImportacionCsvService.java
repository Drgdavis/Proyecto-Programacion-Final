package com.prograiv.importaciones.service;

import com.prograiv.importaciones.client.CatalogoClient;
import com.prograiv.importaciones.dto.ImportacionDetalleResponse;
import com.prograiv.importaciones.exception.ImportacionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImportacionCsvService {

    private static final Logger log =
            LoggerFactory.getLogger(ImportacionCsvService.class);

    private final CsvProductosReader lector;
    private final CatalogoClient catalogo;
    private final RegistroImportacionesService registro;
    private final HistorialImportacionesService historial;

    public ImportacionCsvService(
            CsvProductosReader lector,
            CatalogoClient catalogo,
            RegistroImportacionesService registro,
            HistorialImportacionesService historial) {

        this.lector = lector;
        this.catalogo = catalogo;
        this.registro = registro;
        this.historial = historial;
    }

    public ImportacionDetalleResponse importar(MultipartFile archivo) {
        String nombre = nombreSeguro(archivo.getOriginalFilename());
        Long id = registro.iniciar(nombre);

        log.info("Iniciando importacion id={}", id);

        try {
            var lectura = lector.leer(archivo);

            registro.preparar(
                    id,
                    lectura.total(),
                    lectura.errores()
            );

            var resultados = catalogo.registrar(lectura.validos());

            registro.finalizar(id, resultados);

            log.info("Importacion completada id={} total={}",
                    id, lectura.total());

            return historial.obtener(id);

        } catch (ResponseStatusException exception) {
            String mensaje = exception.getReason() == null
                    ? "El archivo no es valido."
                    : exception.getReason();

            registro.fallar(id, mensaje);

            log.warn("Archivo rechazado id={} motivo={}", id, mensaje);

            throw new ImportacionException(
                    id,
                    HttpStatus.BAD_REQUEST,
                    mensaje
            );

        } catch (RestClientException exception) {
            String mensaje =
                    "No se pudo confirmar el resultado del catalogo. "
                            + "Puede haber productos registrados; revisa el catalogo "
                            + "antes de reenviar el archivo.";

            registro.fallar(id, mensaje);

            log.error("Fallo de comunicacion con catalogo id={}",
                    id, exception);

            throw new ImportacionException(
                    id,
                    HttpStatus.BAD_GATEWAY,
                    mensaje
            );

        } catch (RuntimeException exception) {
            String mensaje =
                    "No se pudo completar la importacion. "
                            + "Revisa el historial y el catalogo antes de reenviarla.";

            log.error("Fallo interno de importacion id={}", id, exception);

            registro.fallar(id, mensaje);

            throw new ImportacionException(
                    id,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    mensaje
            );
        }
    }

    private String nombreSeguro(String original) {
        if (original == null || original.isBlank()) {
            return "archivo";
        }

        String nombre = original.replace('\\', '/');
        nombre = nombre.substring(nombre.lastIndexOf('/') + 1);

        return nombre.length() > 255
                ? nombre.substring(0, 255)
                : nombre;
    }
}