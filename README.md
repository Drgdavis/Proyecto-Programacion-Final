# Gestión de importación de productos

Aplicación web de Programación IV para importar productos desde archivos CSV,
consultar el catálogo y revisar el historial y los errores de cada carga.

## Componentes

| Componente | Puerto | Función |
|---|---:|---|
| Frontend PHP MVC | 8000 | Interfaz de usuario |
| Spring Cloud Gateway | 8080 | Entrada a las API |
| Catálogo | 8081 | Productos, categorías y validaciones |
| Importaciones | 8082 | Lectura CSV, coordinación e historial |
| Eureka | 8761 | Registro y descubrimiento de servicios |
| MySQL | 3306 | Bases de catálogo e importaciones |

PHP consume las API mediante Gateway. Importaciones se comunica por HTTP
con el catálogo mediante el nombre de servicio resuelto por Eureka.
Cada microservicio administra su propia base de datos.

## Instalación

Requisitos: Git, JDK 21, PHP 8.2 o superior con cURL y MySQL 8.4.
Maven está incluido mediante los wrappers de cada servicio.

```powershell
git clone https://github.com/Drgdavis/Proyecto-Programacion-Final.git
cd Proyecto-Programacion-Final
```

En MySQL, ejecutar `database/01_catalogo.sql` y `database/02_importaciones.sql`.
Crear los usuarios mediante las plantillas `database/usuario_catalogo.example.sql`
y `database/usuario_importaciones.example.sql`, con contraseñas locales.
Las variables de conexión son `CATALOGO_DB_PASSWORD` e `IMPORTACIONES_DB_PASSWORD`.

La configuración completa está en [MySQL](database/README.md) y
[operación y diagnóstico](docs/OPERACION_Y_ERRORES.md).

## Ejecución

Desde la raíz del repositorio, ejecutar cada comando en una terminal distinta,
iniciando Eureka antes que los demás servicios:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Eureka
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Catalogo
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Importaciones
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Gateway
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Frontend
```

Los servicios de negocio solicitan su contraseña si la variable no está definida.
Las terminales deben permanecer abiertas. La interfaz está en <http://localhost:8000/>.

## Archivos CSV

Formato UTF-8, separado por comas y con los siguientes encabezados:

```csv
codigo,nombre,categoria,precio,existencia
DEMO001,Teclado USB,Perifericos,250.00,10
```

El código debe ser único, la categoría debe existir, el precio debe ser mayor que
cero y la existencia debe ser un entero no negativo. El límite de archivo es 5 MB.
La interfaz incluye una plantilla descargable; los casos de prueba están en
[pruebas/csv](pruebas/csv/README.md).

## API y pruebas

- [Swagger de catálogo](http://localhost:8081/swagger-ui.html).
- [Swagger de importaciones](http://localhost:8082/swagger-ui.html).
- [Panel de Eureka](http://localhost:8761/).
- [Instrucciones y resultados de pruebas](docs/PRUEBAS_INTEGRACION.md).

## Documentación

- [Informe técnico](docs/INFORME_FINAL.pdf).
- [Manual de usuario](docs/MANUAL_USUARIO.md).
- [Arquitectura](docs/ARQUITECTURA.md).
- [Contratos de las API](docs/API.md).
- [Swagger y logs](docs/SWAGGER_Y_LOGS.md).
- [Evidencias](docs/evidencias/README.md).
- [Limitaciones técnicas](docs/LIMITACIONES.md).
