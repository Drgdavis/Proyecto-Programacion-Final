param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("Eureka", "Catalogo", "Importaciones", "Gateway", "Frontend")]
    [string]$Servicio
)

$ErrorActionPreference = "Stop"
$raizProyecto = Split-Path -Parent $PSScriptRoot
$modulosServicio = @{
    Eureka = "eureka-server"
    Catalogo = "catalogo-service"
    Importaciones = "importaciones-service"
    Gateway = "gateway-service"
    Frontend = "frontend-php"
}

if ($Servicio -ne "Frontend") {
    if (!$env:JAVA_HOME -or !(Test-Path -LiteralPath (Join-Path $env:JAVA_HOME "bin\java.exe"))) {
        $jdkLocal = Get-ChildItem -LiteralPath (Join-Path $env:USERPROFILE ".jdks") -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like "*21*" -and (Test-Path -LiteralPath (Join-Path $_.FullName "bin\java.exe")) } |
            Select-Object -First 1
        if ($jdkLocal) { $env:JAVA_HOME = $jdkLocal.FullName }
    }
    if ($env:JAVA_HOME) { $env:Path = (Join-Path $env:JAVA_HOME "bin") + ";" + $env:Path }
    if (!(Get-Command java -ErrorAction SilentlyContinue)) {
        throw "Instala JDK 21 y configura JAVA_HOME antes de iniciar los servicios."
    }
}

$variableClave = switch ($Servicio) {
    "Catalogo" { "CATALOGO_DB_PASSWORD" }
    "Importaciones" { "IMPORTACIONES_DB_PASSWORD" }
    default { $null }
}

if ($variableClave -and ![Environment]::GetEnvironmentVariable($variableClave, "Process")) {
    $claveServicio = Read-Host "Contrasena MySQL para $Servicio" -AsSecureString
    $claveTextoServicio = [System.Net.NetworkCredential]::new("", $claveServicio).Password
    [Environment]::SetEnvironmentVariable($variableClave, $claveTextoServicio, "Process")
    Remove-Variable claveTextoServicio
}

Push-Location (Join-Path $raizProyecto $modulosServicio[$Servicio])
try {
    if ($Servicio -eq "Frontend") {
        $phpComando = Get-Command php -ErrorAction SilentlyContinue
        $phpEjecutable = if ($phpComando) { $phpComando.Source } else { "C:\tools\php\php.exe" }
        if (!(Test-Path -LiteralPath $phpEjecutable)) { throw "No se encontro PHP." }
        $modulosPhp = @(& $phpEjecutable -m)
        if ($modulosPhp -notcontains "curl") { throw "Habilita extension=curl en php.ini." }
        Write-Host "Frontend: http://localhost:8000/ - detener con Ctrl+C."
        & $phpEjecutable -d upload_max_filesize=5M -d post_max_size=6M -S localhost:8000 -t public
    } else {
        Write-Host "Iniciando $Servicio. La terminal debe permanecer abierta."
        & .\mvnw.cmd spring-boot:run
    }
    if ($LASTEXITCODE -ne 0) { throw "$Servicio termino con codigo $LASTEXITCODE. El detalle del error aparece en las lineas anteriores." }
} finally {
    Pop-Location
}
