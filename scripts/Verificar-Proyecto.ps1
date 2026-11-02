param([switch]$Integracion)

$ErrorActionPreference = "Stop"
$raizVerificacion = Split-Path -Parent $PSScriptRoot
$jdkVerificacion = $env:JAVA_HOME

if (!$jdkVerificacion) {
    $jdkDetectado = Get-ChildItem -LiteralPath (Join-Path $env:USERPROFILE ".jdks") -Directory -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -like "*21*" } | Select-Object -First 1
    if ($jdkDetectado) { $jdkVerificacion = $jdkDetectado.FullName }
}
if ($jdkVerificacion) {
    $env:JAVA_HOME = $jdkVerificacion
    $env:Path = (Join-Path $jdkVerificacion "bin") + ";" + $env:Path
}

foreach ($moduloVerificacion in @("catalogo-service", "importaciones-service", "gateway-service", "eureka-server")) {
    Push-Location (Join-Path $raizVerificacion $moduloVerificacion)
    try {
        Write-Host "Verificando $moduloVerificacion" -ForegroundColor Cyan
        & .\mvnw.cmd -B verify
        if ($LASTEXITCODE -ne 0) { throw "Fallo Maven en $moduloVerificacion." }
    } finally { Pop-Location }
}

$phpEncontrado = Get-Command php -ErrorAction SilentlyContinue
$phpVerificacion = if ($phpEncontrado) { $phpEncontrado.Source } else { "C:\tools\php\php.exe" }
if (!(Test-Path -LiteralPath $phpVerificacion)) { throw "No se encontro PHP para revisar sintaxis." }

Get-ChildItem -LiteralPath (Join-Path $raizVerificacion "frontend-php") -Recurse -Filter "*.php" |
    ForEach-Object {
        & $phpVerificacion -l $_.FullName
        if ($LASTEXITCODE -ne 0) { throw "Error PHP en $($_.FullName)." }
    }

if ($Integracion) {
    & (Join-Path $raizVerificacion "pruebas\Verificar-Integracion.ps1")
}

Write-Host "Verificacion terminada correctamente." -ForegroundColor Green
