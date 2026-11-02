# Frontend PHP MVC

Interfaz de seis pantallas. No usa credenciales MySQL ni accede a las bases. ApiClient llama al Gateway definido en config/app.php.

## Ejecutar

PHP con cURL habilitado. Desde raíz: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Frontend`. Abre http://localhost:8000/. El script limita carga a5MB y cuerpo a6MB.

## Estructura

public/index.php es la entrada. app/Controllers toma entradas y selecciona vistas. app/Models realiza operaciones del dominio por HTTP mediante app/Core/ApiClient.php. app/Views presenta y escapa valores.

Rutas r: inicio, productos, producto, importar, procesar-importacion, historial, resultado-importacion. La carga POST transmite CURLFile, y el éxito redirige303 al resultado persistido. Los errores conservan el enlace al intento cuando existe.

## Verificar

Lint mediante scripts/Verificar-Proyecto.ps1 y pruebas manuales de seis pantallas. No se utiliza curl_close en PHP8.5, donde está obsoleto. El servidor integrado es para desarrollo/demostración local.

Ver [manual](../docs/MANUAL_USUARIO.md), [capturas](../docs/evidencias/README.md).
