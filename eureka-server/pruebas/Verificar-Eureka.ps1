param(
    [string]$Eureka = "http://localhost:8761"
)

$ErrorActionPreference = "Stop"
$Eureka = $Eureka.TrimEnd("/")

function Confirmar-Servicio {
    param(
        [string]$Nombre,
        [string]$Ruta
    )

    $registro = Invoke-RestMethod `
        -Uri "$Eureka/eureka/apps/$Nombre" `
        -Headers @{ Accept = "application/json" } `
        -TimeoutSec 10

    $instancias = @($registro.application.instance)
    $fallos = @()

    foreach ($instancia in $instancias) {
        if ($instancia.status -ne "UP") {
            continue
        }

        if ([string]$instancia.port.'@enabled' -ne "true") {
            continue
        }

        $puertoServicio = [int]$instancia.port.'$'
        $hostServicio = [string]$instancia.hostName

        if ([string]::IsNullOrWhiteSpace($hostServicio) `
            -or $puertoServicio -lt 1 `
            -or $puertoServicio -gt 65535) {
            continue
        }

        $direccionServicio = [System.UriBuilder]::new(
                "http",
                $hostServicio,
                $puertoServicio,
                $Ruta
        ).Uri.AbsoluteUri

        try {
            $respuesta = Invoke-WebRequest `
                -Uri $direccionServicio `
                -Headers @{ Accept = "application/json" } `
                -UseBasicParsing `
                -TimeoutSec 10

            if ([int]$respuesta.StatusCode -eq 200) {
                Write-Host (
                "[OK] {0}: registrado y disponible en {1}" -f
                $Nombre,
                $direccionServicio
                ) -ForegroundColor Green

                return
            }
        } catch {
            $fallos += (
            "{0}: {1}" -f
            $direccionServicio,
            $_.Exception.Message
            )
        }
    }

    $detalle = $fallos -join " | "

    throw (
    "No se encontro una instancia disponible de {0}. {1}" -f
    $Nombre,
    $detalle
    )
}

$disponibilidad = Invoke-RestMethod `
    -Uri "$Eureka/actuator/health/readiness" `
    -TimeoutSec 10

if ($disponibilidad.status -ne "UP") {
    throw "Eureka todavia no esta listo."
}

Write-Host "[OK] Eureka disponible" -ForegroundColor Green

Confirmar-Servicio `
    -Nombre "CATALOGO-SERVICE" `
    -Ruta "/api/categorias"

Confirmar-Servicio `
    -Nombre "IMPORTACIONES-SERVICE" `
    -Ruta "/api/importaciones"

Confirmar-Servicio `
    -Nombre "GATEWAY-SERVICE" `
    -Ruta "/api/categorias"

Write-Host ""
Write-Host "Registro y disponibilidad verificados." -ForegroundColor Green