# Eureka Server

Registro de instancias, puerto8761. No es una base de datos de productos ni un proxy de peticiones.

## Ejecutar

Desde raíz: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Eureka`. Abrir http://localhost:8761/. Los clientes se registran por nombre, host, puerto e instanceId.

Este servidor local no necesita registrarse en sí mismo ni descargar otro registro. La clase principal lleva @EnableEurekaServer.

## Qué interpretar

UP significa que el registro conoce una instancia y recibió su estado; no garantiza disponibilidad instantánea. Las renovaciones y cachés explican demoras al detener/reiniciar. La protección de Eureka puede conservar instancias ante renovaciones insuficientes.

Un enlace actuator/info puede devolver404 si no se expone ese endpoint: comprobar la ruta real de la API para decidir si el servicio funciona.

` .\mvnw.cmd -B verify` ejecuta prueba de contexto. Log en logs/eureka-server.log. La clase principal declara package com.prograiv.eureka; la carpeta física heredada termina en eureka_server.

Ver [operación](../docs/OPERACION_Y_ERRORES.md).
