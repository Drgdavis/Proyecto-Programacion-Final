# Manual de usuario

## Antes de comenzar

Abre <http://localhost:8000/> con MySQL y los cinco procesos iniciados. Los indicadores se consultan en vivo: si los servicios están detenidos puede mostrarse una pantalla de error.

## 1. Inicio

Muestra cantidad de productos, categorías e importaciones, además del resultado más reciente. Los botones llevan a importar y consultar productos. Las importaciones fallidas también forman parte del historial.

## 2. Importar CSV

1. Entra a Importar CSV.
2. Lee los encabezados y reglas del formulario.
3. Selecciona un archivo .csv de hasta 5 MB, guardado en UTF-8.
4. Pulsa Importar productos una vez y espera la respuesta.
5. Si termina, se abre la pantalla de resultado guardado.
6. Si el archivo es rechazado, lee el mensaje y consulta el intento cuando exista un enlace.

Un archivo separado por punto y coma no cumple el formato. Los nombres de categorías deben existir: en la instalación inicial son Perifericos y Monitores. Para una nueva importación utiliza códigos nuevos si deseas que sean aceptados.

## 3. Resultado

Presenta nombre, fecha, estado, total, aceptados y rechazados. La tabla indica número de registro CSV, código, aceptación, enlace al producto creado y motivo.

- Todas muestra cada resultado disponible.
- Solo errores filtra las filas rechazadas.
- Actualizar la pantalla realiza un GET; no reenvía el archivo exitoso.
- Nueva importación abre un formulario vacío.
- Volver al historial permite consultar otros intentos.

COMPLETADA_CON_ERRORES significa que la evaluación terminó. FALLIDA indica un archivo no procesable o un fallo técnico. En este último caso puede haber filas sin confirmar: no deben sumarse como rechazos de validación.

## 4. Productos

Busca por código, nombre o categoría. Los criterios se combinan. Por página permite elegir de 1 a 100 filas. Buscar inicia desde la primera página; Anterior y Siguiente conservan los filtros.

Limpiar elimina los criterios. Una búsqueda sin coincidencias muestra un mensaje y no es un error del servidor.

## 5. Detalle

Ver detalle muestra id, código, nombre, categoría, precio y existencia. También se puede acceder desde una fila aceptada en el resultado de importación. Un id inexistente muestra un error comprensible.

## 6. Historial

Lista archivos desde el intento más reciente y muestra estado y contadores. Ver resultado recupera la importación y sus errores. La paginación permite recorrer más de diez intentos.

## Si algo falla

| Situación | Acción |
|---|---|
| No se pudo contactar al servicio | Revisa los procesos; no repitas inmediatamente una carga |
| Código ya registrado | Corrige el CSV o consulta el producto existente |
| Categoría inexistente | Utiliza una categoría disponible |
| Encabezados incorrectos | Corrige nombres, orden y separador |
| Archivo vacío | Selecciona un archivo con encabezado y productos |
| Resultado no confirmado | Revisa historial y catálogo antes de volver a importar |
| 503 en Gateway | Comprueba registro UP y espera actualización del descubrimiento |

No necesitas escribir comandos SQL para utilizar las pantallas. Las tareas de instalación y recuperación se describen en la guía de operación.
