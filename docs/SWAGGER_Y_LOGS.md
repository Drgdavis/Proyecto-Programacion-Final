# Swagger y Logback

Estado: VERIFICADO

## Documentación de las APIs

| Servicio | Interfaz | Definición |
|---|---|---|
| Catálogo | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| Importaciones | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |

Springdoc 3.0.3 inspecciona controladores y DTO. OpenApiConfig define título, descripción y versión; Tag agrupa operaciones y Operation explica su finalidad.

PHP sigue utilizando Gateway. Swagger directo es una herramienta de documentación y comprobación, no la configuración de comunicación del frontend.

## Pruebas observadas

- GET /api/productos?page=0&size=2 respondió 200, dos elementos y total 6 en esa ejecución.
- GET /api/importaciones respondió 200 con los cuatro intentos existentes en ese momento.
- GET /api/importaciones/3 respondió 200, total 6, aceptados 2 y rechazados 4.
- GET /api/importaciones/3/errores respondió 200 con cuatro rechazos.
- Los documentos OpenAPI generados corresponden a ambas APIs.
- Las cantidades históricas cambian al ejecutar más pruebas; no son valores fijos del programa.

## Logging

Spring Boot utiliza Logback mediante sus starters. Cada proceso genera su archivo dentro de su directorio de trabajo:

- catalogo-service/logs/catalogo-service.log
- importaciones-service/logs/importaciones-service.log
- gateway-service/logs/gateway-service.log
- eureka-server/logs/eureka-server.log

Rotación: archivo de 10 MB, hasta 7 días de historial y 100 MB de archivos históricos. Los directorios logs se excluyen de Git.

ApiRequestLoggingFilter registra método, ruta, estado y duración para /api/.
ImportacionCsvService añade id del intento, finalización, rechazo de archivo y fallos.

Ejemplos observados:

```text
INFO HTTP metodo=GET ruta=/api/productos estado=200 duracionMs=140
INFO HTTP metodo=GET ruta=/api/importaciones/3/errores estado=200 duracionMs=11
WARN HTTP metodo=GET ruta=/api/productos estado=400 duracionMs=10
```

Los tiempos son observaciones de una ejecución, no requisitos de rendimiento.

## Niveles

- INFO: operación normal.
- WARN: solicitud inválida o situación recuperable.
- ERROR: fallo técnico que debe investigarse.

Los avisos de Springdoc sobre endpoints de documentación habilitados indican su disponibilidad. La configuración se conserva para la demostración local.

## Referencias

- https://springdoc.org/
- https://docs.spring.io/spring-boot/4.0/reference/features/logging.html
