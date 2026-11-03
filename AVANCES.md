# Estado técnico del proyecto

## Funcionalidad implementada

- Catálogo MySQL con categorías, productos, filtros, detalle y paginación.
- Registro por lote con validación y aceptación parcial.
- Importación CSV UTF-8 con historial persistente y resultados por fila.
- Registro de servicios en Eureka y acceso mediante Gateway.
- Frontend PHP MVC con consulta, carga, historial y resultados.
- Swagger de las dos APIs y logs de peticiones con rotación.

## Verificaciones

- 32 pruebas Maven aprobadas entre los cuatro módulos: catálogo 5, importaciones 22, Gateway 4 y Eureka 1.
- 33 comprobaciones de integración HTTP registradas.
- Revisión de sintaxis de PHP y scripts PowerShell.
- Evidencias de consultas HTTP y de las seis pantallas.

## Correcciones recientes

- Manejo de UncheckedIOException en archivos CSV mal formados.
- Traducción de errores de resolución del catálogo a errores de comunicación.
- Configuración H2 de alcance test para pruebas aisladas.
- Plantillas SQL con marcadores de contraseña.
- Scripts de arranque y verificación.

## Entorno de ejecución

La verificación se realizó en el entorno local existente. La instalación en otra computadora no se ha comprobado. Las instrucciones de configuración y arranque están en README.md y las limitaciones técnicas en docs/LIMITACIONES.md.

Consultar README.md para ejecución, docs/PRUEBAS_INTEGRACION.md para validación y docs/LIMITACIONES.md para el alcance actual.
