<?php

declare(strict_types=1);

class ApiClient
{
    public function __construct(private string $baseUrl)
    {
    }

    public function get(string $ruta, array $parametros = []): array
    {
        return $this->solicitar('GET', $ruta, $parametros);
    }

    public function subirArchivo(
        string $ruta,
        string $temporal,
        string $nombre
    ): array {
        return $this->solicitar('POST', $ruta, [], [
            'archivo' => new CURLFile(
                $temporal,
                'text/csv',
                $nombre
            ),
        ]);
    }

    private function solicitar(
        string $metodo,
        string $ruta,
        array $parametros = [],
        ?array $formulario = null
    ): array {
        $url = $this->baseUrl . $ruta;

        if ($parametros !== []) {
            $url .= '?' . http_build_query($parametros);
        }

        $conexion = curl_init($url);

        if ($conexion === false) {
            throw new ApiException(
                'No se pudo iniciar la solicitud.',
                502
            );
        }

        try {
            curl_setopt_array($conexion, [
                CURLOPT_RETURNTRANSFER => true,
                CURLOPT_CONNECTTIMEOUT => 5,
                CURLOPT_TIMEOUT => 90,
                CURLOPT_HTTPHEADER => [
                    'Accept: application/json',
                ],
            ]);

            if ($metodo === 'POST') {
                curl_setopt($conexion, CURLOPT_POST, true);
                curl_setopt(
                    $conexion,
                    CURLOPT_POSTFIELDS,
                    $formulario
                );
            }

            $respuesta = curl_exec($conexion);

            $estado = (int) curl_getinfo(
                $conexion,
                CURLINFO_RESPONSE_CODE
            );

            if ($respuesta === false) {
                error_log('Error de Gateway: ' . curl_error($conexion));

                throw new ApiException(
                    'No se pudo confirmar la respuesta del servicio. '
                    . 'Si enviaste un archivo, revisa el historial '
                    . 'antes de repetir la carga.',
                    502
                );
            }
        } finally {
            unset($conexion);
        }

        try {
            $datos = json_decode(
                $respuesta,
                true,
                512,
                JSON_THROW_ON_ERROR
            );
        } catch (JsonException $exception) {
            throw new ApiException(
                'No se pudo interpretar la respuesta del servicio. '
                . 'Revisa el historial antes de repetir una carga.',
                502
            );
        }

        if (!is_array($datos)) {
            throw new ApiException(
                'El servicio devolvio una respuesta inesperada.',
                502
            );
        }

        if ($estado >= 400) {
            $mensaje = $datos['mensaje']
                ?? 'El servicio no pudo completar la solicitud.';

            if (!is_string($mensaje)) {
                $mensaje = 'El servicio no pudo completar la solicitud.';
            }

            $importacionId = filter_var(
                $datos['importacionId'] ?? null,
                FILTER_VALIDATE_INT,
                ['options' => ['min_range' => 1]]
            );

            throw new ApiException(
                $mensaje,
                $estado >= 500 ? 502 : $estado,
                $importacionId === false ? null : $importacionId
            );
        }

        return $datos;
    }
}