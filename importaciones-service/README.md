# Importaciones

Servicio CSV e historial, puerto8082, nombre lógico importaciones-service. Solo administra importaciones_db; los productos se registran mediante HTTP al catálogo descubierto por Eureka.

## Ejecutar

Ejecutar ../database/02_importaciones.sql, preparar usuario y definir IMPORTACIONES_DB_PASSWORD. Desde raíz: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Importaciones`.

## Contrato y flujo

POST /api/importaciones recibe multipart con archivo. GET de la misma ruta devuelve historial paginado. GET /{id} incluye resumen/resultados y GET /{id}/errores filtra rechazados. Swagger: http://localhost:8082/swagger-ui.html.

CSV UTF-8, coma, encabezados exactos codigo,nombre,categoria,precio,existencia. El lector diferencia errores de archivo y de fila. El coordinador persiste intento antes de llamar al catálogo, correlaciona por itemId y consolida resultados.

CatalogoHttpConfig separa el RestClient.Builder normal del @LoadBalanced. CatalogoClient usa http://catalogo-service; no sustituirlo por localhost:8081.

## Estados y fallos

PROCESANDO, COMPLETADA, COMPLETADA_CON_ERRORES y FALLIDA. Ante respuesta incierta del catálogo,502 con advertencia e ID. No reintentar sin consultar: no hay transacción distribuida ni idempotencia durable.

## Pruebas

` .\mvnw.cmd -B verify`:22 pruebas, incluye contexto, lector, correlación HTTP y errores de comunicación. H2 solo para test. La captura de UncheckedIOException del parser requiere reinicio para tener efecto en procesos existentes.

Ver [API](../docs/API.md), [operación](../docs/OPERACION_Y_ERRORES.md) y [limitaciones](../docs/LIMITACIONES.md).
