# Estado técnico del proyecto

## Funcionalidad implementada

- Catálogo MySQL con categorías, productos, filtros, detalle y paginación.
- Registro por lote con validación y aceptación parcial.
- Importación CSV UTF-8 con historial persistente y resultados por fila.
- Registro de servicios en Eureka y acceso mediante Gateway.
- Frontend PHP MVC con consulta, carga, historial y resultados.
- Swagger de las dos APIs y logs de peticiones con rotación.

## Verificaciones

- 29 pruebas Maven aprobadas entre los cuatro módulos.
- 33 comprobaciones de integración HTTP registradas.
- Revisión de sintaxis de PHP y scripts PowerShell.
- Evidencias de consultas HTTP y de las seis pantallas.

## Correcciones recientes

- Manejo de UncheckedIOException en archivos CSV mal formados.
- Traducción de errores de resolución del catálogo a errores de comunicación.
- Configuración H2 de alcance test para pruebas aisladas.
- Plantillas SQL con marcadores de contraseña.
- Scripts de arranque y verificación.

## Comprobaciones pendientes

- Instalación completa en una máquina o entorno limpio.
- Rotación de credenciales si se utilizaron valores compartidos anteriormente.
- Reinicio de procesos existentes para cargar las últimas clases compiladas.

Consultar README.md para ejecución, docs/PRUEBAS_INTEGRACION.md para validación y docs/LIMITACIONES.md para el alcance actual.
