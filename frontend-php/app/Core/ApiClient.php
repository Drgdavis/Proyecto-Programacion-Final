<?php

declare(strict_types=1);

class ApiClient
{
    public function __construct(private string $baseUrl)
    {
    }

    public function get(string $ruta, array $parametros = []): array
    {
        $url = $this->baseUrl . $ruta;

        if ($parametros !== []) {
            $url .= '?' . http_build_query($parametros);
        }

        $conexion = curl_init($url);

        if ($conexion === false) {
            throw new ApiException(
                'No se pudo iniciar la consulta.',
                502
            );
        }

        try {
            curl_setopt_array($conexion, [
                CURLOPT_RETURNTRANSFER => true,
                CURLOPT_CONNECTTIMEOUT => 5,
                CURLOPT_TIMEOUT => 45,
                CURLOPT_HTTPHEADER => [
                    'Accept: application/json',
                ],
            ]);

            $respuesta = curl_exec($conexion);
            $estado = (int) curl_getinfo(
                $conexion,
                CURLINFO_RESPONSE_CODE
            );

            if ($respuesta === false) {
                error_log('Error de Gateway: ' . curl_error($conexion));

                throw new ApiException(
                    'No se pudo contactar al servicio. Intenta nuevamente.',
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
                'El servicio devolvio una respuesta que no se pudo interpretar.',
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

            throw new ApiException(
                $mensaje,
                $estado >= 500 ? 502 : $estado
            );
        }

        return $datos;
    }
}