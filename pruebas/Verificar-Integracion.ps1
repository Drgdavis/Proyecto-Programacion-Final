param(
    [string]$Gateway = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$Gateway = $Gateway.TrimEnd("/")

$prefijoPrueba = "IT" + [guid]::NewGuid().ToString("N").Substring(0, 10).ToUpperInvariant()

$carpetaTemporal = Join-Path $env:TEMP (
    "prograiv-integracion-" + $prefijoPrueba
)

New-Item -ItemType Directory -Path $carpetaTemporal | Out-Null

$utf8 = [System.Text.UTF8Encoding]::new($false)
$script:comprobaciones = 0

function Confirmar {
    param(
        [string]$Nombre,
        $Actual,
        $Esperado
    )

    if ($Actual -ne $Esperado) {
        throw "$Nombre. Esperado: $Esperado. Recibido: $Actual."
    }

    $script:comprobaciones++
    Write-Host "[OK] $Nombre" -ForegroundColor Green
}

function Consultar-Api {
    param(
        [string]$Ruta,
        [string]$Archivo = ""
    )

    $argumentosCurl = @(
        "-sS",
        "--connect-timeout", "5",
        "--max-time", "100",
        "-H", "Accept: application/json",
        "-w", "`n%{http_code}"
    )

    if ($Archivo -ne "") {
        $argumentosCurl += @("-F", "archivo=@$Archivo")
    }

    $argumentosCurl += ($Gateway + $Ruta)

    $salidaCurl = @(& curl.exe @argumentosCurl)

    if ($LASTEXITCODE -ne 0) {
        throw "No se pudo contactar a la API: $Ruta"
    }

    if ($salidaCurl.Count -lt 2) {
        throw "La API no devolvio una respuesta interpretable: $Ruta"
    }

    $codigoHttp = [int]$salidaCurl[-1]

    $cuerpoJson = (
        $salidaCurl | Select-Object -SkipLast 1
    ) -join "`n"

    try {
        $datosRespuesta = ConvertFrom-Json -InputObject $cuerpoJson
    } catch {
        throw "La respuesta no contiene JSON valido: $Ruta"
    }

    return [PSCustomObject]@{
        Estado = $codigoHttp
        Datos  = $datosRespuesta
    }
}

function Crear-Csv {
    param(
        [string]$Nombre,
        [string]$Contenido
    )

    $rutaCsv = Join-Path $carpetaTemporal $Nombre

    [System.IO.File]::WriteAllText(
        $rutaCsv,
        $Contenido,
        $utf8
    )

    return $rutaCsv
}

function Confirmar-ArchivoRechazado {
    param(
        [string]$Nombre,
        [string]$Archivo
    )

    $respuesta = Consultar-Api "/api/importaciones" $Archivo

    Confirmar "$Nombre responde 400" $respuesta.Estado 400

    if ($null -eq $respuesta.Datos.importacionId) {
        throw "$Nombre no devolvio el identificador del intento."
    }

    $idIntento = $respuesta.Datos.importacionId

    $detalle = Consultar-Api (
        "/api/importaciones/" + $idIntento
    )

    Confirmar "$Nombre conserva el intento" $detalle.Estado 200

    Confirmar "$Nombre queda FALLIDA" `
        $detalle.Datos.importacion.estado `
        "FALLIDA"

    if ([string]::IsNullOrWhiteSpace(
        $detalle.Datos.importacion.mensaje
    )) {
        throw "$Nombre no conserva un mensaje explicativo."
    }
}

Write-Host "Gateway: $Gateway"
Write-Host "Prefijo de esta ejecucion: $prefijoPrueba"
Write-Host "Se crearan dos productos validos y varios intentos de importacion."
Write-Host ""

# 1. Consultas y validacion de parametros.

$respuesta = Consultar-Api "/api/categorias"
Confirmar "Categorias disponibles" $respuesta.Estado 200

$respuesta = Consultar-Api "/api/productos?page=-1"
Confirmar "Pagina negativa rechazada" $respuesta.Estado 400

# 2. Archivo completamente valido, incluyendo una coma entre comillas.

$archivoValido = Crear-Csv "valido.csv" @"
codigo,nombre,categoria,precio,existencia
${prefijoPrueba}A,"Teclado, integracion",Perifericos,250.50,8
${prefijoPrueba}B,Monitor integracion,Monitores,1200.00,3
"@

$respuesta = Consultar-Api "/api/importaciones" $archivoValido

Confirmar "CSV valido responde 201" $respuesta.Estado 201
Confirmar "CSV valido total" $respuesta.Datos.importacion.total 2
Confirmar "CSV valido aceptados" $respuesta.Datos.importacion.aceptados 2
Confirmar "CSV valido rechazados" $respuesta.Datos.importacion.rechazados 0

Confirmar "CSV valido queda COMPLETADA" `
    $respuesta.Datos.importacion.estado `
    "COMPLETADA"

$idImportacionValida = $respuesta.Datos.importacion.id

# 3. Persistencia y lectura correcta de campos entre comillas.

$respuesta = Consultar-Api (
    "/api/productos?codigo=" + $prefijoPrueba + "A"
)

Confirmar "Producto guardado una sola vez" `
    $respuesta.Datos.totalElementos `
    1

Confirmar "Se conserva la coma del nombre" `
    $respuesta.Datos.contenido[0].nombre `
    "Teclado, integracion"

# 4. Codigo que ya existe en catalogo.

$archivoDuplicado = Crear-Csv "duplicado.csv" @"
codigo,nombre,categoria,precio,existencia
${prefijoPrueba}A,Producto repetido,Perifericos,300.00,5
"@

$respuesta = Consultar-Api "/api/importaciones" $archivoDuplicado

Confirmar "Lote duplicado responde 201" $respuesta.Estado 201
Confirmar "Duplicado no aceptado" $respuesta.Datos.importacion.aceptados 0
Confirmar "Duplicado rechazado" $respuesta.Datos.importacion.rechazados 1

# 5. Categoria inexistente, precio no convertible,
# existencia decimal y columna faltante.

$archivoInvalido = Crear-Csv "filas_invalidas.csv" @"
codigo,nombre,categoria,precio,existencia
${prefijoPrueba}X1,Categoria incorrecta,NoExiste,100.00,2
${prefijoPrueba}X2,Precio incorrecto,Perifericos,"12,50",2
${prefijoPrueba}X3,Existencia incorrecta,Perifericos,100.00,1.5
${prefijoPrueba}X4,Columna faltante,Perifericos,100.00
"@

$respuesta = Consultar-Api "/api/importaciones" $archivoInvalido

Confirmar "Filas invalidas responden 201" $respuesta.Estado 201
Confirmar "Filas invalidas total" $respuesta.Datos.importacion.total 4
Confirmar "Filas invalidas aceptados" $respuesta.Datos.importacion.aceptados 0
Confirmar "Filas invalidas rechazados" $respuesta.Datos.importacion.rechazados 4

Confirmar "Filas invalidas conservan sus resultados" `
    @($respuesta.Datos.resultados).Count `
    4

foreach ($filaResultado in $respuesta.Datos.resultados) {
    if ([string]::IsNullOrWhiteSpace($filaResultado.motivo)) {
        throw "Una fila rechazada no tiene motivo."
    }
}

# 6. Archivo vacio.

$archivoVacio = Join-Path $carpetaTemporal "vacio.csv"

[System.IO.File]::WriteAllBytes(
    $archivoVacio,
    [byte[]]@()
)

Confirmar-ArchivoRechazado "Archivo vacio" $archivoVacio

# 7. Archivo que contiene solamente encabezados.

$soloEncabezados = Crear-Csv "solo_encabezados.csv" (
    "codigo,nombre,categoria,precio,existencia`r`n"
)

Confirmar-ArchivoRechazado "Solo encabezados" $soloEncabezados

# 8. Separador y encabezados incorrectos.

$encabezadosIncorrectos = Crear-Csv "encabezados_incorrectos.csv" @"
codigo;nombre;categoria;precio;existencia
PRUEBA;Producto;Perifericos;100.00;2
"@

Confirmar-ArchivoRechazado `
    "Encabezados incorrectos" `
    $encabezadosIncorrectos

# 9. Archivo con bytes que no forman UTF-8 valido.

$archivoCodificacion = Join-Path $carpetaTemporal "utf8_invalido.csv"

$bytesInicio = [System.Text.Encoding]::UTF8.GetBytes(
    "codigo,nombre,categoria,precio,existencia`r`n"
)

$bytesFinal = [System.Text.Encoding]::UTF8.GetBytes(
    ",Producto,Perifericos,100.00,2`r`n"
)

$bytesInvalidos = [byte[]](
    $bytesInicio + [byte[]]@(255) + $bytesFinal
)

[System.IO.File]::WriteAllBytes(
    $archivoCodificacion,
    $bytesInvalidos
)

Confirmar-ArchivoRechazado `
    "Codificacion incorrecta" `
    $archivoCodificacion

# 10. Consulta posterior del resultado correcto.

$respuesta = Consultar-Api (
    "/api/importaciones/" + $idImportacionValida
)

Confirmar "Detalle persistido responde 200" $respuesta.Estado 200

Confirmar "Detalle conserva dos filas" `
    @($respuesta.Datos.resultados).Count `
    2

$respuesta = Consultar-Api (
    "/api/importaciones/" + $idImportacionValida + "/errores"
)

Confirmar "Errores del archivo valido responden 200" `
    $respuesta.Estado `
    200

Confirmar "Archivo valido no tiene errores" `
    @($respuesta.Datos).Count `
    0

Write-Host ""
Write-Host "PRUEBAS COMPLETADAS: $script:comprobaciones comprobaciones." `
    -ForegroundColor Green

Write-Host "Importacion valida: $idImportacionValida"
Write-Host "Archivos de prueba: $carpetaTemporal"
Write-Host ("Consulta web: http://localhost:8000/index.php?r=productos&codigo=" + $prefijoPrueba)