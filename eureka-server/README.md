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


## Estado del servidor

- GET /actuator/health: estado general.
- GET /actuator/health/readiness: disponibilidad para recibir solicitudes.
- GET /actuator/info: nombre y descripción del componente.

Estos endpoints comprueban el servidor Eureka. No garantizan que todos
los microservicios registrados estén disponibles; cada servicio debe
comprobarse mediante su propia API.

## Verificación del registro

Con Eureka, catálogo, importaciones y Gateway iniciados, ejecutar desde
la raíz del repositorio:

    powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\eureka-server\pruebas\Verificar-Eureka.ps1

El script consulta las instancias registradas y utiliza sus hosts y
puertos para comprobar las APIs reales. No crea datos de negocio.

Si no encuentra un servicio, revisar su arranque, registro y logs.
Si el servicio acaba de iniciar, esperar a que aparezca registrado y
volver a ejecutar la comprobación.