# Contratos HTTP

Base del frontend: `http://localhost:8080`. Las interfaces Swagger se consultan directamente en los puertos de los servicios.

## Catálogo

| Método | Ruta | Entrada | Éxito |
|---|---|---|---|
| GET | /api/categorias | Sin cuerpo | 200, lista de categorías |
| GET | /api/productos | codigo, nombre, categoriaId, page, size | 200, página |
| GET | /api/productos/{id} | Identificador positivo | 200, producto |
| POST | /api/productos/lote | JSON con productos | 200, resumen y resultados |

Los filtros de texto son parciales y no distinguen mayúsculas. Los filtros suministrados se combinan. page empieza en 0; size está entre 1 y 100; el orden es por id.

### Página

```json
{"contenido":[],"pagina":0,"tamanio":10,"totalElementos":0,"totalPaginas":0}
```

### Producto

```json
{"id":1,"codigo":"P001","nombre":"Teclado","categoriaId":1,"categoria":"Perifericos","precio":350.00,"existencia":20}
```

### Lote

```json
{
  "productos": [
    {"itemId":"2","codigo":"P001","nombre":"Teclado","categoria":"Perifericos","precio":350.00,"existencia":20},
    {"itemId":"3","codigo":"P002","nombre":"Precio incorrecto","categoria":"Perifericos","precio":0,"existencia":1}
  ]
}
```

```json
{
  "total":2,"aceptados":1,"rechazados":1,
  "resultados":[
    {"itemId":"2","aceptado":true,"productoId":1,"motivo":null},
    {"itemId":"3","aceptado":false,"productoId":null,"motivo":"El precio debe ser mayor que cero."}
  ]
}
```

Un lote parcialmente rechazado sigue teniendo una respuesta HTTP correcta: el resultado por elemento expresa sus rechazos. Un sobre vacío, elementos nulos o itemId repetidos invalidan la solicitud antes de escribir. itemId correlaciona solicitudes y respuestas, no es el código ni el id persistido.

## Importaciones

| Método | Ruta | Entrada | Éxito |
|---|---|---|---|
| POST | /api/importaciones | multipart, campo archivo | 201, resumen y filas |
| GET | /api/importaciones | page, size | 200, historial paginado |
| GET | /api/importaciones/{id} | Identificador positivo | 200, resumen y filas |
| GET | /api/importaciones/{id}/errores | Identificador positivo | 200, solo rechazadas |

El historial se ordena por id descendente. Los identificadores dependen de la base instalada; los ejemplos no garantizan que una importación concreta tenga id 1.

### Enviar CSV desde PowerShell

```powershell
curl.exe -sS -F "archivo=@C:\ruta\productos.csv" "http://localhost:8080/api/importaciones"
```

No establezcas manualmente un Content-Type JSON para este POST: el cuerpo contiene un archivo multipart.

### Resultado

```json
{
  "importacion":{
    "id":3,"archivo":"lote.csv","fecha":"2026-10-31T17:42:42",
    "estado":"COMPLETADA_CON_ERRORES","total":2,
    "aceptados":1,"rechazados":1,"mensaje":null
  },
  "resultados":[
    {"fila":2,"codigo":"P001","aceptado":true,"productoId":1,"motivo":null},
    {"fila":3,"codigo":"P002","aceptado":false,"productoId":null,"motivo":"La categoria indicada no existe."}
  ]
}
```

### Error con intento registrado

```json
{"estado":400,"mensaje":"Encabezados esperados: codigo,nombre,categoria,precio,existencia","importacionId":4}
```

### Códigos de estado

| Código | Interpretación |
|---|---|
| 200 | Consulta o evaluación del lote realizada |
| 201 | Importación procesada y guardada, incluso con rechazos por fila |
| 400 | Archivo, sobre o parámetros inválidos |
| 404 | Identificador o ruta inexistente |
| 405 | Método HTTP no permitido |
| 413 | Archivo o solicitud supera el límite |
| 415 | Tipo de contenido HTTP no admitido |
| 500 | Fallo interno no clasificado como comunicación |
| 502 | Importaciones no pudo confirmar el resultado del catálogo |
| 503 | Gateway no encuentra una instancia disponible |

Las excepciones propias del Gateway se presentan como JSON con los campos estado y mensaje, conservando el código HTTP. Las respuestas normales de error de los microservicios se transmiten sin reemplazarlas. Una solicitud demasiado grande o sin archivo puede ser rechazada antes de crear un historial. Por eso no todos los errores tienen importacionId.

## Reglas de formato y persistencia

- codigo: hasta 50 caracteres; nombre: hasta 150; categoria: hasta 100.
- precio: punto decimal, mayor que cero, hasta 10 enteros y 2 decimales.
- existencia: entero no negativo dentro del rango de Java Integer.
- Encabezados exactos y ordenados.
- UTF-8 estricto, con soporte de BOM.
- Los errores por fila no descartan otras filas válidas.
- Un CSV con comillas sin cerrar se rechaza completo: no puede determinarse de forma fiable la separación de registros.
- El historial conserva fila, código y motivo; no almacena el archivo completo.
- Las fechas actuales son LocalDateTime del servidor, sin offset explícito.
