# Evidencias reales

Capturas tomadas el 31 de octubre de 2026 sobre la aplicación local ya ejecutada. Los datos son de práctica: ocho productos y trece intentos en el momento de captura. Las cantidades pueden cambiar al ejecutar pruebas.

| Captura | Qué permite comprobar |
|---|---|
| [Inicio](01-inicio.jpg) | Resumen y navegación |
| [Productos](02-productos.jpg) | Tabla, filtros y consulta |
| [Detalle](03-detalle.jpg) | Producto persistido WEB001 |
| [Importar](04-importar.jpg) | Formulario y contrato CSV |
| [Historial](05-historial.jpg) | Intentos persistidos y estados |
| [Resultado](06-resultado.jpg) | Resumen de carga mixta y resultados |

[Consultas HTTP reales](consultas-reales.json): cuatro GET con estado200, URL y respuesta completa. Se realizaron contra Gateway y los procesos conectados a MySQL. No se subieron nuevos archivos durante la captura.

[Resultados Maven](maven-resultados.json): resumen extraído de los XML Surefire, con fecha de generación y resultado por suite. Los reportes originales quedan en target y se regeneran con verify.

El resultado histórico 3 contiene dos filas aceptadas y cuatro rechazadas. Los intentos12/13 documentan pruebas manuales de catálogo caído; el13 recibió502 después de corregir el manejo de comunicación. No se fabrica una captura de instalación limpia ni de una prueba no realizada.

Para entregar evidencia adicional, repetir la demostración con códigos nuevos y capturar la pantalla completa de resultado, un400 en Swagger y la línea de log correspondiente. No incluir claves.
