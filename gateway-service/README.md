# Gateway

Entrada HTTP del frontend, puerto8080. Registra gateway-service en Eureka y enruta por nombres lógicos.

## Rutas actuales

- /api/categorias y rutas de productos → lb://catalogo-service.
- /api/importaciones y subrutas → lb://importaciones-service.

Las definiciones exactas están en src/main/resources/application.properties. No se usa exposición automática de todos los servicios registrados. Una ruta ausente devuelve404; una ruta sin instancia disponible puede devolver503. Los puertos directos continúan accesibles según la red.

## Ejecutar y verificar

Desde raíz: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Gateway`. Iniciar Eureka y servicios de negocio antes; esperar actualización de descubrimiento.

Probar http://localhost:8080/api/categorias y http://localhost:8080/api/importaciones. Comparar con consultas directas solo para diagnóstico.

` .\mvnw.cmd -B verify` comprueba contexto sin depender del Eureka activo. Esa prueba no sustituye una llamada de extremo a extremo. Log en logs/gateway-service.log.

Clase principal declara package com.prograiv.gateway aunque la carpeta física heredada termina en gateway_service.

Ver [arquitectura](../docs/ARQUITECTURA.md) y [diagnóstico](../docs/OPERACION_Y_ERRORES.md).


## Respuestas de error

GatewayErrorHandler convierte las excepciones propias del Gateway
en respuestas JSON con estado y mensaje.

Ejemplo de una ruta inexistente:

    {"estado":404,"mensaje":"La ruta solicitada no existe en el Gateway."}

Si el balanceador no encuentra una instancia disponible, conserva
el código 503 y devuelve un mensaje comprensible.

Los detalles internos de las excepciones no se incluyen en el cuerpo.
Las respuestas enviadas normalmente por los microservicios se conservan.

Las pruebas de GatewayErrorHandler comprueban los estados 404, 503 y 500.