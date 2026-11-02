# Operación, arranque y diagnóstico

## Requisitos

Windows con PowerShell, Git, JDK 21, PHP 8.2+ con cURL y MySQL 8.4. La primera compilación Maven requiere Internet; después utiliza la caché de dependencias.

La instalación de referencia está en C:\Users\davis\IdeaProjects\Proyecto-Programacion-Final. En otra computadora clona el repositorio donde prefieras; los scripts calculan la raíz a partir de su ubicación.

## Instalar una copia

```powershell
git clone https://github.com/Drgdavis/Proyecto-Programacion-Final.git
cd Proyecto-Programacion-Final
java -version
```

Si Java no está en PATH, seleccionarlo en IntelliJ no configura automáticamente una terminal externa. Configura JAVA_HOME apuntando al directorio del JDK, no a bin.

PHP de referencia: C:\tools\php\php.exe. En php.ini habilita cURL, extension_dir apuntando a ext, upload_max_filesize=5M y post_max_size=6M. Se puede iniciar también con límites -d, como hace el script.

Configura MySQL siguiendo [database/README.md](../database/README.md).

## Arranque en terminales separadas

Desde la raíz ejecuta `scripts/Iniciar-Servicio.ps1 -Servicio NOMBRE`, mediante PowerShell:

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

La terminal solicitará la clave de catalogo_app si CATALOGO_DB_PASSWORD no está definida. Importaciones usa IMPORTACIONES_DB_PASSWORD. Cada proceso PowerShell tiene sus propias variables: no asumas que una terminal nueva hereda variables creadas en otra.

El script permanece ejecutándose y muestra los logs. No cierres esa terminal. Para Git o pruebas utiliza otra.

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

Para importaciones cambia el nombre de variable a IMPORTACIONES_DB_PASSWORD. No introduzcas la contraseña root cuando se solicita la de un usuario de aplicación.

## Verificación inicial

1. Eureka muestra CATALOGO-SERVICE, IMPORTACIONES-SERVICE y GATEWAY-SERVICE UP.
2. localhost:8081/api/categorias responde directamente.
3. localhost:8080/api/categorias responde mediante Gateway.
4. localhost:8080/api/importaciones responde.
5. localhost:8000 muestra la interfaz.
6. Ambas páginas Swagger abren.

Tras un reinicio, las cachés y renovaciones pueden tardar varias decenas de segundos en reflejar una instancia. El estado UP es información de registro; confirma también una solicitud real.

## Diagnóstico por síntoma

| Síntoma | Comprobación y solución |
|---|---|
| java o php no se reconoce | Revisa PATH. Para PHP utiliza C:\tools\php\php.exe |
| Access denied 1045 | Comprueba usuario, clave y variable del mismo proceso. Reestablece la clave en Workbench si procede |
| Unable to determine Dialect | Busca primero el error JDBC anterior; no lo ocultes fijando un dialecto |
| Connection refused a 8761 | Inicia Eureka y conserva su terminal |
| No instances available for localhost | Comprueba los dos RestClient.Builder: normal Primary y balanceado para catálogo |
| 404 directo y por Gateway | Revisa mappings y paquete del controlador |
| Funciona en 8082 pero 404 en 8080 | Revisa la ruta explícita de Gateway y reinícialo |
| 503 en Gateway | Comprueba servicio directo, registro UP, tiempo de caché y logs del Gateway |
| 502 al importar | Revisa catálogo, su registro y el intento; no reintentes ciegamente |
| Port already in use | Ya existe un proceso escuchando. Identifícalo; no inicies copias indiscriminadamente |
| class is public, should be declared... | El nombre del archivo debe coincidir exactamente con la clase |
| class, interface, enum, or record expected | Revisa package duplicado y código pegado fuera de la clase |
| Estilos ausentes | El archivo debe ser public/assets/app.css, no app.css.php |
| Deprecated curl_close | El cliente actual libera la referencia con unset |
| Required id en Swagger | Escribe un id existente en el campo antes de Execute |
| Maven no encuentra SpringBootConfiguration | Comprueba package o classes de SpringBootTest |

## Eureka y preservación

El aviso de renovaciones bajas puede permanecer después de detener clientes o suspender Windows. No se soluciona deshabilitando protecciones por rutina. Comprueba solicitudes, instancias, estado y logs. Los enlaces actuator/info del panel pueden responder 404 porque no se incluyó Actuator; eso no demuestra que la API de negocio esté caída.

## Apagado

Detén PHP y los clientes con Ctrl+C; detén Eureka al final. Las bases persisten. No uses comandos de borrado ni git reset para apagar aplicaciones.

## Recuperación después de una caída

Reinicia primero la dependencia caída. Espera el registro y prueba el endpoint directo; luego el de Gateway. Conserva el intento fallido y su mensaje como evidencia. El sistema no incluye reconciliación automática de un lote cuya respuesta se perdió.
