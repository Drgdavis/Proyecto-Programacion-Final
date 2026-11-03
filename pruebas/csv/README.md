# Archivos CSV de práctica

El script Verificar-Integracion.ps1 genera archivos temporales con prefijos únicos y conserva su ubicación al terminar. Los archivos de esta carpeta permiten probar manualmente.

| Archivo | Propósito |
|---|---|
| demo_valido.csv | Dos productos válidos; uno con coma y comillas escapadas |
| demo_mixto.csv | Dos válidos, precio0, categoría inexistente, duplicado y stock negativo |
| demo_tipos_invalidos.csv | Requeridos, decimal de stock, desbordamiento y precisión de precio |
| demo_solo_encabezados.csv | Error de archivo sin productos |
| demo_encabezado_incorrecto.csv | Separador incompatible |
| demo_comillas_invalidas.csv | Estructura CSV mal formada |
| demo_vacio.csv | Archivo sin bytes |

Los códigos DEMO son fijos. Antes de una demostración nueva, se requiere cambiar el prefijo para distinguir los duplicados de ejecuciones anteriores de los errores del programa. Perifericos y Monitores deben existir.

Los archivos de UTF-8 inválido se generan como bytes dentro del script de integración; no se guardan como texto supuestamente legible. Un archivo mal formado necesita el servicio reiniciado con la versión corregida para comprobar400.
