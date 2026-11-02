# Requisitos y cierre de entrega

Corte: 31 de octubre de 2026. Fuente: enunciado `[PrograIV]ProyectoDeFinalDeCurso (1).pdf`, cuatro páginas, y verificaciones del proyecto. Esta lista describe resultados observados; una casilla implementada no equivale a una calificación.

## Matriz de cumplimiento

| Requisito | Implementación / evidencia | Estado |
|---|---|---|
| PHP con MVC | Front controller, Controllers, Models, Views y ApiClient | Implementado |
| Inicio con resumen | Conteos de productos, categorías e importaciones | Probado en navegador |
| Carga CSV | Formulario multipart, validación y API de importaciones | Probado desde PHP |
| Resultado por fila y errores | Resumen, enlaces a productos, filtro de rechazados | Probado |
| Catálogo con filtros y paginación | Código, nombre y categoría; páginas desde 0 en API | Probado |
| Detalle de producto | Consulta y vista por identificador | Probado |
| Historial paginado | Persistencia, resumen y enlace al resultado | Probado |
| Dos microservicios de negocio | Catálogo e importaciones con bases separadas | Implementado y funcionando |
| Comunicación HTTP real | CatalogoClient y RestClient con balanceador | Probada mediante carga CSV |
| Descubrimiento | Eureka con registro de ambos servicios | Probado |
| Entrada por Gateway | PHP consume puerto 8080 y rutas explícitas | Probado |
| Validaciones de productos | Obligatoriedad, unicidad, categoría, precio y stock | Pruebas automatizadas y manuales |
| CSV UTF-8 y encabezados | Decodificación estricta, BOM y parser RFC4180 | Pruebas automatizadas |
| Manejo de errores | Errores 400/404; disponibilidad 502/503; intento persistido | Probado; ver limitaciones |
| SQL y usuarios | Scripts y plantillas sin claves reales | Preparados |
| Swagger/OpenAPI | Documentación ejecutable de las dos APIs | Probado con HTTP 200 |
| Logback | Archivos rotativos y filtro de método/ruta/estado/duración | Probado con registros 200 y 400 |
| Pruebas de integración | Script PowerShell: 33 comprobaciones | Ejecución real aprobada |
| Pruebas Maven | 29 pruebas entre cuatro módulos | verify aprobado |
| README y manuales | Operación, arquitectura, API, usuario y diagnóstico | Preparados |
| Cinco commits por componente principal | Conteo conservador independiente por carpeta | **Pendiente en varios componentes** |

## Pendientes que no se pueden sustituir con documentación

1. Revisar el conteo de Git después del commit de Swagger. El catálogo supera cinco; los otros componentes requieren avances reales adicionales. No borrar ni reconstruir la historia para simular trabajo.
2. Ejecutar la instalación en otra computadora o en una base limpia con credenciales locales. Los tests H2 no demuestran por sí solos compatibilidad completa con un MySQL recién instalado.
3. Cambiar las contraseñas si se utilizaron valores compartidos en scripts anteriores. No registrar las nuevas claves en Git.
4. Reiniciar los servicios después de compilar cambios: un proceso Java ya iniciado conserva las clases con las que arrancó.

## Paquete a entregar

- Código de los cinco componentes, wrappers y POM.
- Scripts de esquema y plantillas de usuarios.
- CSV de prueba y script de integración.
- README, documentación y evidencias reales.
- Enlace al repositorio con su historia real.

Excluir `.git` del ZIP si el repositorio se entrega por enlace; excluir `target`, `logs`, `.idea`, contraseñas, cachés y archivos locales. El ZIP no sustituye el historial del repositorio.

## Comprobación de la entrega

Los resultados de verificación se describen en [pruebas](PRUEBAS_INTEGRACION.md). El [manual de usuario](MANUAL_USUARIO.md) cubre las pantallas y [operación](OPERACION_Y_ERRORES.md) explica instalación, arranque y diagnóstico. Consultar también las [limitaciones técnicas](LIMITACIONES.md).
