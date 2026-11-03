# Limitaciones técnicas

## Resultados distribuidos

No hay transacción distribuida ni clave de idempotencia de importación. Un POST al catálogo puede haber guardado productos aunque su respuesta no llegue. Por ello se conserva FALLIDA, se advierte al usuario y no se reintenta automáticamente.

Un cierre abrupto puede dejar un intento PROCESANDO. No existe un trabajo de recuperación de esos intentos. Los contadores en FALLIDA reflejan únicamente lo confirmado.

## Archivo y trazabilidad

El historial conserva fila lógica, código y motivo, no el archivo completo ni todas las columnas originales. La fila corresponde a un registro CSV: los saltos dentro de comillas no crean otro producto. La corrección de un precio se realiza en el archivo original, identificando el registro por fila y código.

Se procesan archivos de hasta 5 MB de forma síncrona y en memoria. No se implementa una cola de grandes cargas. Las longitudes y dos decimales son decisiones coherentes con el esquema SQL.

## Operación

Las fechas son LocalDateTime sin zona explícita. Las pantallas muestran la hora registrada por el servidor.

No se añadió autenticación porque no forma parte del alcance funcional indicado. La falta de una ruta Gateway no impide una conexión directa al puerto del servicio. Este proyecto es una demostración local, no una configuración de despliegue público.

El servidor PHP integrado es para desarrollo y demostración. La verificación con H2 aísla tests; no sustituye las comprobaciones MySQL reales ni demuestra que todas las diferencias de motores estén cubiertas.

## Configuración de entornos

Una contraseña apareció anteriormente en una plantilla SQL. Su reemplazo actual no borra versiones anteriores: si se utilizó, debe rotarse desde MySQL.

El arranque en otra computadora requiere crear las bases y usuarios con credenciales propias. Las verificaciones documentadas corresponden al entorno local existente.
