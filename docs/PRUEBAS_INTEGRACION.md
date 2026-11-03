# Pruebas y evidencia

## Resultado real del corte del 31 de octubre

| Nivel | Resultado |
|---|---|
| Maven catálogo | 5 pruebas, sin errores ni fallos |
| Maven importaciones | 22 pruebas, sin errores ni fallos |
| Maven Gateway | 1 prueba de contexto, aprobada |
| Maven Eureka | 1 prueba de contexto, aprobada |
| Total Maven | **29 pruebas aprobadas**, cuatro módulos con BUILD SUCCESS |
| Script HTTP contra MySQL | **33 comprobaciones aprobadas**, ejecución registrada |
| PHP / navegador | Consulta, filtros, detalle, carga, resultado e historial comprobados |
| Catálogo no disponible | Intento 13: respuesta 502 e historial FALLIDA; recuperación posterior del catálogo |

Las ejecuciones Maven de este cierre terminaron aproximadamente a las 22:40 del 31 de octubre de 2026, hora de Ciudad de México. Los reportes Surefire se generan en cada `target/surefire-reports`; no se versionan los binarios.

La primera prueba manual de catálogo caído devolvió 500 (intento 12); después de corregir la traducción del error de transporte devolvió 502 (intento 13). El primer resultado corresponde al comportamiento anterior a la corrección.

## 1. Verificación sin base de producción

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Verificar-Proyecto.ps1
```

El script ejecuta Maven verify por módulo y la revisión sintáctica de PHP. Las pruebas de negocio usan H2 solo en `src/test/resources`; el arranque normal sigue usando MySQL. Maven puede necesitar descargar dependencias la primera vez.

### Qué prueban los tests de catálogo

- Un lote mixto conserva los productos válidos y rechaza inválidos y duplicados.
- Un sobre con itemId repetido se rechaza antes de escribir.
- Los filtros se combinan con AND y la paginación mantiene orden estable.
- Consultar un identificador inexistente produce el error correspondiente.
- El contexto de Spring arranca.

### Qué prueban los tests de importaciones

- UTF-8, BOM, comas y comillas dentro de valores.
- Campos requeridos, longitudes, precio y rango/tipo de existencia.
- Archivos vacíos, solo encabezados, encabezados incorrectos, CSV con comillas mal cerradas y UTF-8 inválido.
- Correlación por itemId aunque catálogo cambie el orden de respuestas.
- Respuesta incompleta del catálogo.
- Falta de instancia disponible traducida a error de comunicación.
- Arranque del contexto.

Se corrigió un caso real durante estas pruebas: Commons CSV puede lanzar UncheckedIOException al consumir un CSV mal formado. Ahora el lector lo convierte en un error de archivo 400. Esta versión requiere reiniciar importaciones para probarla en el proceso HTTP existente.

## 2. Integración HTTP con bases MySQL

Inicia MySQL y los cinco procesos siguiendo [operación](OPERACION_Y_ERRORES.md). Luego, desde la raíz:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\pruebas\Verificar-Integracion.ps1
```

La opción `Verificar-Proyecto.ps1 -Integracion` ejecuta ambos niveles. El script genera un prefijo único y archivos temporales. **Crea dos productos y varios intentos persistidos**; su ejecución requiere una base de práctica. No elimina registros existentes.

Comprobaciones observadas: categorías disponibles; página negativa 400; archivo válido 201/COMPLETADA; coma conservada; duplicados rechazados; cuatro filas inválidas conservadas; vacío, solo encabezados, encabezados incorrectos y UTF-8 inválido 400 con intento FALLIDA; detalle recuperable y errores del archivo válido vacíos.

La ejecución mostrada terminó con: `PRUEBAS COMPLETADAS: 33 comprobaciones.` No confundir ese número con 33 métodos JUnit.

## 3. Prueba manual de disponibilidad

1. Confirma que el catálogo responde por Gateway.
2. Detener únicamente catálogo con Ctrl+C.
3. Enviar un archivo con un código nuevo.
4. Comprobar el error 502 de importaciones y su importacionId.
5. Consultar el intento: FALLIDA. Los contadores pueden no representar un resultado confirmado.
6. Reiniciar catálogo, esperar el registro y la actualización de cachés, y consultar productos.
7. Antes de reenviar, comprobar si el código ya existe. Un fallo de red no garantiza que el catálogo no escribió.

Si Gateway no descubre importaciones, devuelve 503 antes de llegar al coordinador. Un estado UP en Eureka puede ser una concesión aún no vencida y no garantiza disponibilidad instantánea.

## 4. Casos manuales de aceptación

| Caso | Esperado | Comprobación |
|---|---|---|
| Catálogo normal | 200 y productos | GET /api/productos |
| page=-1 | 400 | API, Swagger y log WARN |
| size=0 o 101 | 400 | API |
| Producto inexistente | 404 | API y pantalla de error |
| CSV mixto | 201 y resumen por fila | Formulario y resultado |
| Repetir código | Fila rechazada | Resultado y catálogo sin duplicar |
| Categoría inexistente | Fila rechazada | Motivo legible |
| UTF-8 inválido | 400 e intento FALLIDA | Historial |
| Archivo vacío / solo encabezado | 400 | Mensaje y enlace al intento |
| Archivo >5 MB | 413 en API, mensaje en PHP | Verificar límite en entorno local |
| Método incorrecto | 405 | GET de ruta solo POST |
| Ruta sin Gateway | 404 | No confundir con seguridad de red |

Los casos de esta tabla sin resultado específico en las evidencias son instrucciones reproducibles, no afirmaciones de ejecución.

## 5. Revisar pruebas sin contaminar la demostración

Utilizar códigos nuevos para la demostración, guardar el ID devuelto y explicar por qué reenviar un CSV válido puede producir duplicados rechazados. El contador del inicio refleja datos reales de la base y cambia con cada ejecución.

Ver [evidencias](evidencias/README.md) y [limitaciones](LIMITACIONES.md).

## Actualización del 2 de noviembre de 2026

El corte anterior se conserva como evidencia histórica.

- Maven: 32 pruebas aprobadas, sin fallos ni errores.
- Distribución: catálogo 5, importaciones 22, Gateway 4 y Eureka 1.
- Los cuatro módulos finalizaron con BUILD SUCCESS.
- Revisión sintáctica de PHP completada sin errores.
- Frontend: 30 comprobaciones aprobadas de pantallas, parámetros,
  métodos HTTP y plantilla CSV.
- Eureka: registro y disponibilidad de catálogo, importaciones y
  Gateway comprobados mediante Verificar-Eureka.ps1.

Estas verificaciones se realizaron en el entorno local existente.
No equivalen a una instalación comprobada en otra computadora.