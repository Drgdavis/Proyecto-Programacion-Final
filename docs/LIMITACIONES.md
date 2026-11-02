# Límites conocidos y decisiones pendientes

## Resultados distribuidos

No hay transacción distribuida ni clave de idempotencia de importación. Un POST al catálogo puede haber guardado productos aunque su respuesta no llegue. Por ello se conserva FALLIDA, se advierte al usuario y no se reintenta automáticamente.

Un cierre abrupto puede dejar un intento PROCESANDO. No existe un trabajo de recuperación de esos intentos. Los contadores en FALLIDA reflejan únicamente lo confirmado.

## Archivo y trazabilidad

El historial conserva fila lógica, código y motivo, no el archivo completo ni todas las columnas originales. La fila corresponde a un registro CSV: los saltos dentro de comillas no crean otro producto. Para corregir un precio, vuelve al archivo usando fila y código.

Se procesan archivos de hasta 5 MB de forma síncrona y en memoria. No se implementa una cola de grandes cargas. Las longitudes y dos decimales son decisiones coherentes con el esquema SQL.

## Operación

Las fechas son LocalDateTime sin zona explícita. Las pantallas muestran la hora registrada por el servidor.

No se añadió autenticación porque no forma parte del alcance funcional indicado. La falta de una ruta Gateway no impide una conexión directa al puerto del servicio. Este proyecto es una demostración local, no una configuración de despliegue público.

El servidor PHP integrado es para desarrollo y demostración. La verificación con H2 aísla tests; no sustituye las comprobaciones MySQL reales ni demuestra que todas las diferencias de motores estén cubiertas.

## Entrega y Git

El requisito de cinco commits por componente se evalúa aparte de la funcionalidad. Mantener fechas reales y explicar el historial existente; no fabricar progresión ni afirmar que un archivo aún no versionado ya está entregado.

Una contraseña apareció anteriormente en una plantilla SQL. Su reemplazo actual no borra versiones anteriores: si se utilizó, debe rotarse desde MySQL.

El arranque limpio sobre una máquina ajena requiere crear las bases y usuarios con credenciales propias. No se borraron ni recrearon las bases actuales durante la revisión para simular una instalación nueva.
