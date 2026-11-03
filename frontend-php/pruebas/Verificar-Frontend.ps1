param(
    [string]$Frontend = "http://localhost:8000"
)

$ErrorActionPreference = "Stop"
$Frontend = $Frontend.TrimEnd("/")

if (!(Get-Command curl.exe -ErrorAction SilentlyContinue)) {
    throw "No se encontro curl.exe."
}

$carpetaFrontend = Join-Path $env:TEMP (
"prograiv-frontend-" + [guid]::NewGuid().ToString("N")
)

New-Item -ItemType Directory -Path $carpetaFrontend | Out-Null

$script:numeroSolicitud = 0
$script:comprobaciones = 0

function Confirmar {
    param(
        [string]$Nombre,
        [bool]$Condicion
    )

    if (!$Condicion) {
        throw "Fallo la comprobacion: $Nombre"
    }

    $script:comprobaciones++
    Write-Host "[OK] $Nombre" -ForegroundColor Green
}

function Consultar-Web {
    param(
        [string]$Ruta,
        [string]$Metodo = "GET"
    )

    $script:numeroSolicitud++

    $archivoCuerpo = Join-Path $carpetaFrontend (
    "respuesta-{0}.txt" -f $script:numeroSolicitud
    )

    $archivoCabeceras = Join-Path $carpetaFrontend (
    "cabeceras-{0}.txt" -f $script:numeroSolicitud
    )

    $argumentos = @(
        "--silent",
        "--show-error",
        "--globoff",
        "--connect-timeout", "5",
        "--max-time", "100",
        "--request", $Metodo,
        "--dump-header", $archivoCabeceras,
        "--output", $archivoCuerpo,
        "--write-out", "%{http_code}",
        ($Frontend + $Ruta)
    )

    $codigo = & curl.exe @argumentos

    if ($LASTEXITCODE -ne 0) {
        throw "No se pudo consultar $Ruta. Comprobar el estado de los procesos."
    }

    return [pscustomobject]@{
        Estado = [int]$codigo
        Cuerpo = [System.IO.File]::ReadAllText(
                $archivoCuerpo,
                [System.Text.Encoding]::UTF8
        )
        Cabeceras = [System.IO.File]::ReadAllText(
                $archivoCabeceras,
                [System.Text.Encoding]::UTF8
        )
    }
}

function Confirmar-Pagina {
    param(
        [string]$Ruta,
        [int]$Esperado = 200,
        [string]$Marca = "",
        [string]$Metodo = "GET"
    )

    $respuesta = Consultar-Web -Ruta $Ruta -Metodo $Metodo

    Confirmar (
    "$Metodo $Ruta responde $Esperado"
    ) ($respuesta.Estado -eq $Esperado)

    if ($Marca -ne "") {
        Confirmar (
        "Contenido esperado en $Ruta"
        ) ($respuesta.Cuerpo.Contains($Marca))
    }

    return $respuesta
}

Write-Host "Frontend: $Frontend"
Write-Host "Evidencias temporales: $carpetaFrontend"
Write-Host ""

# Pantallas principales.

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=inicio" `
    -Marca "Resumen del sistema"

$productos = Confirmar-Pagina `
    -Ruta "/index.php?r=productos&size=1" `
    -Marca "<h1>Productos</h1>"

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=importar" `
    -Marca "/plantilla_productos.csv"

$historial = Confirmar-Pagina `
    -Ruta "/index.php?r=historial" `
    -Marca "<h1>Historial</h1>"

# Obtener enlaces reales, sin depender de identificadores fijos.

$enlaceProducto = [regex]::Match(
        $productos.Cuerpo,
        'href="([^"]*r=producto(?:&amp;|&)id=[0-9]+[^"]*)"'
)

if (!$enlaceProducto.Success) {
    throw "Se necesita al menos un producto para comprobar su detalle."
}

$rutaProducto = [System.Net.WebUtility]::HtmlDecode(
        $enlaceProducto.Groups[1].Value
)

$null = Confirmar-Pagina `
    -Ruta $rutaProducto `
    -Marca "DETALLE DE PRODUCTO"

$enlaceResultado = [regex]::Match(
        $historial.Cuerpo,
        'href="([^"]*r=resultado-importacion(?:&amp;|&)id=[0-9]+[^"]*)"'
)

if (!$enlaceResultado.Success) {
    throw "Se necesita al menos una importacion para comprobar su resultado."
}

$rutaResultado = [System.Net.WebUtility]::HtmlDecode(
        $enlaceResultado.Groups[1].Value
)

$null = Confirmar-Pagina `
    -Ruta $rutaResultado `
    -Marca "Resultados por fila"

$null = Confirmar-Pagina `
    -Ruta ($rutaResultado + "&ver=errores") `
    -Marca "filas rechazadas"

# Busqueda sin coincidencias.

$codigoInexistente = "VACIO" + [guid]::NewGuid().ToString("N")

$null = Confirmar-Pagina `
    -Ruta ("/index.php?r=productos&codigo=" + $codigoInexistente) `
    -Marca "No hay productos"

# Parametros y rutas invalidas.

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=productos&page=-1" `
    -Esperado 400

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=productos&size=101" `
    -Esperado 400

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=producto&id=abc" `
    -Esperado 400

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=resultado-importacion&id=0" `
    -Esperado 400

$null = Confirmar-Pagina `
    -Ruta "/index.php?r=pagina_que_no_existe" `
    -Esperado 404

# La ruta de procesamiento admite POST, no GET.

$metodoCarga = Confirmar-Pagina `
    -Ruta "/index.php?r=procesar-importacion" `
    -Esperado 405

Confirmar "La carga informa Allow: POST" (
$metodoCarga.Cabeceras -match '(?im)^Allow:\s*POST\s*$'
)

# Productos admite GET. Este POST no envia datos de negocio.

$metodoProductos = Confirmar-Pagina `
    -Ruta "/index.php?r=productos" `
    -Metodo "POST" `
    -Esperado 405

Confirmar "Productos informa Allow: GET" (
$metodoProductos.Cabeceras -match '(?im)^Allow:\s*GET\s*$'
)

# Plantilla descargable.

$plantilla = Confirmar-Pagina `
    -Ruta "/plantilla_productos.csv"

Confirmar "La plantilla responde como CSV" (
$plantilla.Cabeceras -match '(?im)^Content-Type:\s*text/csv\b'
)

$encabezado = (
($plantilla.Cuerpo -split '\r?\n')[0]
).TrimStart([char]0xFEFF)

Confirmar "Encabezados de plantilla correctos" (
$encabezado -eq "codigo,nombre,categoria,precio,existencia"
)

$filasPlantilla = @(
    ConvertFrom-Csv -InputObject $plantilla.Cuerpo
)

Confirmar "La plantilla contiene dos productos" (
$filasPlantilla.Count -eq 2
)

Confirmar "Se conserva la coma dentro del nombre" (
$filasPlantilla[0].nombre -eq "Teclado, mecanico"
)

Write-Host ""
Write-Host (
"PRUEBAS DEL FRONTEND COMPLETADAS: {0} comprobaciones." -f
$script:comprobaciones
) -ForegroundColor Green

Write-Host "Evidencias temporales: $carpetaFrontend"