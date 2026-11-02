# Gestión de importación de productos — Programación IV

Aplicación PHP MVC y dos microservicios Spring Boot para cargar productos desde CSV, consultar el catálogo y revisar los resultados de cada importación.

## Empieza aquí

1. Revisa [requisitos y estado real de la entrega](docs/REQUISITOS_Y_ENTREGA.md).
2. Configura Java 21, PHP 8.2 o superior con cURL y MySQL 8.4.
3. Ejecuta los scripts de [database](database/README.md).
4. Sigue [el arranque de los cinco procesos](docs/OPERACION_Y_ERRORES.md).
5. Abre <http://localhost:8000/>.
6. Ejecuta las [pruebas](docs/PRUEBAS_INTEGRACION.md).

No se necesita XAMPP. PHP utiliza su servidor local; las bases se ejecutan en MySQL Server. Maven se incluye mediante los wrappers de cada servicio.

## Componentes

| Componente | Puerto local | Responsabilidad |
|---|---:|---|
| Frontend PHP MVC | 8000 | Formularios, consultas y presentación |
| Spring Cloud Gateway | 8080 | Entrada del frontend a las APIs |
| Catálogo | 8081 | Productos, categorías y reglas de registro |
| Importaciones | 8082 | CSV, coordinación HTTP e historial |
| Eureka Server | 8761 | Registro y descubrimiento |
| MySQL | 3306 | Bases `catalogo_db` e `importaciones_db` |

El frontend no consulta MySQL. Importaciones no escribe productos directamente. La llamada interna utiliza `http://catalogo-service`, resuelto mediante Eureka; no fija el puerto del catálogo en el código Java.

## Versiones del proyecto

- Java: 21.
- Spring Boot: 4.0.9.
- Spring Cloud: 2025.1.4.
- Springdoc: 3.0.3, línea compatible con Boot 4.0.
- Apache Commons CSV: 1.14.1.
- PHP probado en esta computadora: 8.5.12, con cURL.
- MySQL probado: 8.4.13.
- H2: únicamente para pruebas Maven, dependencia con alcance `test`; la aplicación usa MySQL.

## Preparación de bases y contraseñas

En Workbench, conectado como administrador:

1. Ejecuta `database/01_catalogo.sql`.
2. Ejecuta `database/02_importaciones.sql`.
3. Usa las plantillas `usuario_catalogo.example.sql` y `usuario_importaciones.example.sql`, sustituyendo el marcador de contraseña en una copia local.
4. Si un usuario ya existe, `CREATE USER IF NOT EXISTS` no cambia su clave: consulta las instrucciones de `database/README.md`.

Las claves se suministran mediante `CATALOGO_DB_PASSWORD` e `IMPORTACIONES_DB_PASSWORD`. No se guardan en el repositorio. Una plantilla anterior contenía una clave concreta: si fue utilizada, cámbiala en MySQL y actualiza las variables de las terminales.

## Arranque

Desde la raíz del repositorio, abre cinco terminales. Ejecuta un comando por terminal:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Eureka
```

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Catalogo
```

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Importaciones
```

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Gateway
```

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Frontend
```

Espera a que Eureka arranque antes de los clientes. Los dos servicios de negocio solicitarán su contraseña si la variable no está definida. Conserva las cinco terminales abiertas; utiliza una sexta para comandos. Para detener un proceso, Ctrl+C en su terminal.

## Formato CSV

UTF-8, separador coma, encabezados exactos en este orden:

```csv
codigo,nombre,categoria,precio,existencia
DEMO001,Teclado USB,Perifericos,250.00,10
DEMO002,"Monitor, 24 pulgadas",Monitores,1500.00,3
```

- Código, nombre y categoría obligatorios.
- Código único en catálogo; categoría existente.
- Precio mayor que cero, hasta 10 enteros y 2 decimales, punto decimal.
- Existencia entera entre 0 y 2147483647.
- Archivo máximo 5 MB; solicitud máxima 6 MB.
- La fila 1 es el encabezado; el primer producto es la fila 2.
- Se admiten comas, comillas escapadas y registros con saltos dentro de campos entrecomillados; la numeración corresponde a registros CSV.

Un archivo válido estructuralmente puede tener filas rechazadas. `COMPLETADA_CON_ERRORES` es un procesamiento terminado, distinto de `FALLIDA`.

## Pantallas

Inicio, carga CSV, resultado por fila, productos con filtros y paginación, detalle de producto e historial paginado.

Consulta el [manual de usuario](docs/MANUAL_USUARIO.md) y las [capturas reales](docs/evidencias/README.md).

## APIs y diagnóstico

- Catálogo Swagger: <http://localhost:8081/swagger-ui.html>
- Importaciones Swagger: <http://localhost:8082/swagger-ui.html>
- Eureka: <http://localhost:8761/>
- Frontend: <http://localhost:8000/>
- [Contratos y ejemplos HTTP](docs/API.md)
- [Swagger y Logback](docs/SWAGGER_Y_LOGS.md)
- [Errores frecuentes y operación](docs/OPERACION_Y_ERRORES.md)

## Pruebas

Compilación, tests y empaquetado de los cuatro servicios:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Verificar-Proyecto.ps1
```

Integración real con los servicios iniciados:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\pruebas\Verificar-Integracion.ps1
```

La segunda prueba crea dos productos e intentos de importación. Utiliza códigos únicos por ejecución. Los tests Maven usan bases efímeras y no necesitan contraseñas de MySQL ni registrar clientes en el Eureka de desarrollo.

## Documentación técnica

- [Arquitectura y decisiones](docs/ARQUITECTURA.md).
- [Limitaciones conocidas](docs/LIMITACIONES.md).

## Alcance de la verificación

Los resultados observados y los casos reproducibles se describen en [pruebas](docs/PRUEBAS_INTEGRACION.md). Los límites técnicos y comprobaciones pendientes se detallan en [limitaciones](docs/LIMITACIONES.md).
