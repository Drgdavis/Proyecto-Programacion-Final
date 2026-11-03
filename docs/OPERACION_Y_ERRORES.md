# Operación, arranque y diagnóstico

## Requisitos

Windows con PowerShell, Git, JDK 21, PHP 8.2+ con cURL y MySQL 8.4. La primera compilación Maven requiere Internet; después utiliza la caché de dependencias.

La instalación de referencia está en C:\Users\davis\IdeaProjects\Proyecto-Programacion-Final. En otra computadora, el repositorio puede clonarse en un directorio local; los scripts calculan la raíz a partir de su ubicación.

## Instalar una copia

```powershell
git clone https://github.com/Drgdavis/Proyecto-Programacion-Final.git
cd Proyecto-Programacion-Final
java -version
```

Si Java no está en PATH, seleccionarlo en IntelliJ no configura automáticamente una terminal externa. JAVA_HOME debe apuntar al directorio del JDK, no a bin.

PHP de referencia: C:\tools\php\php.exe. La configuración php.ini requiere cURL habilitado, extension_dir apuntando a ext, upload_max_filesize=5M y post_max_size=6M. Se puede iniciar también con límites -d, como hace el script.

Configurar MySQL según [database/README.md](../database/README.md).

## Arranque en terminales separadas

Desde la raíz, ejecutar `scripts/Iniciar-Servicio.ps1 -Servicio NOMBRE`, mediante PowerShell:

| Orden | Servicio | Señal esperada |
|---|---|---|
| 1 | Eureka | Started EurekaServerApplication, puerto 8761 |
| 2 | Catalogo | Started CatalogoServiceApplication, puerto 8081 |
| 3 | Importaciones | Started ImportacionesServiceApplication, puerto 8082 |
| 4 | Gateway | Started GatewayServiceApplication, puerto 8080 |
| 5 | Frontend | Servidor PHP escuchando en localhost:8000 |

Ejemplo:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Catalogo
```

La terminal solicitará la clave de catalogo_app si CATALOGO_DB_PASSWORD no está definida. Importaciones usa IMPORTACIONES_DB_PASSWORD. Cada proceso PowerShell tiene sus propias variables: una terminal nueva no hereda las variables creadas dentro de otra terminal.

El script permanece ejecutándose y muestra los logs. La terminal debe permanecer abierta. Git y las pruebas se ejecutan en otra terminal.

## Arranque manual de un servicio

Dentro de la carpeta correspondiente:

```powershell
.\mvnw.cmd spring-boot:run
```

Para definir una contraseña sin mostrarla:

```powershell
$claveLocal = Read-Host "Contrasena del usuario de la aplicacion" -AsSecureString
$env:CATALOGO_DB_PASSWORD = [System.Net.NetworkCredential]::new("", $claveLocal).Password
```

Importaciones requiere la variable IMPORTACIONES_DB_PASSWORD. Cada servicio utiliza la contraseña de su usuario de aplicación, no la contraseña de root.

## Verificación inicial

1. Eureka muestra CATALOGO-SERVICE, IMPORTACIONES-SERVICE y GATEWAY-SERVICE UP.
2. localhost:8081/api/categorias responde directamente.
3. localhost:8080/api/categorias responde mediante Gateway.
4. localhost:8080/api/importaciones responde.
5. localhost:8000 muestra la interfaz.
6. Ambas páginas Swagger abren.

Tras un reinicio, las cachés y renovaciones pueden tardar varias decenas de segundos en reflejar una instancia. El estado UP es información de registro; la disponibilidad también se comprueba mediante una solicitud real.

## Diagnóstico por síntoma

| Síntoma | Comprobación y solución |
|---|---|
| java o php no se reconoce | Comprobar PATH. Para PHP se puede utilizar C:\tools\php\php.exe |
| Access denied 1045 | Comprobar usuario, clave y variable del mismo proceso. Restablecer la clave en Workbench si corresponde |
| Unable to determine Dialect | Revisar primero el error JDBC anterior antes de modificar el dialecto |
| Connection refused a 8761 | Iniciar Eureka y mantener su terminal abierta |
| No instances available for localhost | Comprobar los dos RestClient.Builder: normal Primary y balanceado para catálogo |
| 404 directo y por Gateway | Revisar mappings y paquete del controlador |
| Funciona en 8082 pero 404 en 8080 | Revisar la ruta explícita y reiniciar Gateway |
| 503 en Gateway | Comprobar servicio directo, registro UP, tiempo de caché y logs del Gateway |
| 502 al importar | Revisar catálogo, registro e intento antes de repetir la carga |
| Port already in use | Ya existe un proceso escuchando. Identificar el proceso existente antes de iniciar otra instancia |
| class is public, should be declared... | El nombre del archivo debe coincidir exactamente con la clase |
| class, interface, enum, or record expected | Revisar package duplicado y código pegado fuera de la clase |
| Estilos ausentes | El archivo debe ser public/assets/app.css, no app.css.php |
| Deprecated curl_close | El cliente actual libera la referencia con unset |
| Required id en Swagger | Introducir un id existente en el campo antes de Execute |
| Maven no encuentra SpringBootConfiguration | Comprobar package o classes de SpringBootTest |

## Eureka y preservación

El aviso de renovaciones bajas puede permanecer después de detener clientes o suspender Windows. No se soluciona deshabilitando protecciones por rutina. La comprobación comprende solicitudes, instancias, estado y logs. Los enlaces actuator/info del panel pueden responder 404 porque no se incluyó Actuator; eso no demuestra que la API de negocio esté caída.

## Apagado

El apagado se realiza con Ctrl+C en PHP y los clientes, seguido de Eureka. Las bases persisten. No requiere borrar archivos ni modificar el estado de Git.

## Recuperación después de una caída

La recuperación comienza con el reinicio de la dependencia caída. Tras su registro, se comprueba el endpoint directo y después el de Gateway. El intento fallido y su mensaje permanecen disponibles como evidencia. El sistema no incluye reconciliación automática de un lote cuya respuesta se perdió.
