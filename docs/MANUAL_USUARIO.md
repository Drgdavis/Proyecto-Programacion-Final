# Manual de usuario

## Antes de comenzar

La interfaz está disponible en <http://localhost:8000/> con MySQL y los cinco procesos iniciados. Los indicadores se consultan en vivo: si los servicios están detenidos puede mostrarse una pantalla de error.

## 1. Inicio

Muestra cantidad de productos, categorías e importaciones, además del resultado más reciente. Los botones llevan a importar y consultar productos. Las importaciones fallidas también forman parte del historial.

## 2. Importar CSV
El botón Descargar plantilla CSV proporciona un archivo con los
encabezados y dos productos de ejemplo. Antes de la importación se deben
reemplazar los datos, utilizar códigos nuevos y conservar UTF-8 y el separador coma.

1. Acceder a Importar CSV.
2. Revisar los encabezados y reglas del formulario.
3. Seleccionar un archivo .csv de hasta 5 MB, guardado en UTF-8.
4. Pulsar Importar productos una vez y esperar la respuesta.
5. Si termina, se abre la pantalla de resultado guardado.
6. Si el archivo es rechazado, se muestra un mensaje y se permite consultar el intento cuando exista un enlace.

Un archivo separado por punto y coma no cumple el formato. Los nombres de categorías deben existir: en la instalación inicial son Perifericos y Monitores. La aceptación de nuevos productos requiere códigos que no estén registrados.

## 3. Resultado

Presenta nombre, fecha, estado, total, aceptados y rechazados. La tabla indica número de registro CSV, código, aceptación, enlace al producto creado y motivo.

- Todas muestra cada resultado disponible.
- Solo errores filtra las filas rechazadas.
- Actualizar la pantalla realiza un GET; no reenvía el archivo exitoso.
- Nueva importación abre un formulario vacío.
- Volver al historial permite consultar otros intentos.

COMPLETADA_CON_ERRORES significa que la evaluación terminó. FALLIDA indica un archivo no procesable o un fallo técnico. En este último caso puede haber filas sin confirmar: no deben sumarse como rechazos de validación.

## 4. Productos

La búsqueda admite código, nombre o categoría. Los criterios se combinan. Por página permite elegir de 1 a 100 filas. Buscar inicia desde la primera página; Anterior y Siguiente conservan los filtros.

Limpiar elimina los criterios. Una búsqueda sin coincidencias muestra un mensaje y no es un error del servidor.

## 5. Detalle

Ver detalle muestra id, código, nombre, categoría, precio y existencia. También se puede acceder desde una fila aceptada en el resultado de importación. Un id inexistente muestra un error comprensible.

## 6. Historial

Lista archivos desde el intento más reciente y muestra estado y contadores. Ver resultado recupera la importación y sus errores. La paginación permite recorrer más de diez intentos.

## Si algo falla

| Situación | Acción |
|---|---|
| No se pudo contactar al servicio | Comprobar los procesos antes de repetir una carga |
| Código ya registrado | Corregir el CSV o consultar el producto existente |
| Categoría inexistente | Utilizar una categoría disponible |
| Encabezados incorrectos | Corregir nombres, orden y separador |
| Archivo vacío | Seleccionar un archivo con encabezado y productos |
| Resultado no confirmado | Consultar historial y catálogo antes de volver a importar |
| 503 en Gateway | Comprobar registro UP y esperar la actualización del descubrimiento |

El uso de las pantallas no requiere comandos SQL. Las tareas de instalación y recuperación se describen en la guía de operación.
