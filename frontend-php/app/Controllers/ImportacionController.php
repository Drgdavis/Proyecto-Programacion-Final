<?php

declare(strict_types=1);

class ImportacionController
{
    public function __construct(private ImportacionModel $modelo)
    {
    }

    public function formulario(): void
    {
        $this->mostrarFormulario();
    }

    public function recibir(): void
    {
        try {
            if ((int) ($_SERVER['CONTENT_LENGTH'] ?? 0) > 6 * 1024 * 1024) {
                throw new ApiException(
                    'La solicitud supera el limite de carga permitido.',
                    413
                );
            }

            $archivo = $_FILES['archivo'] ?? null;

            if (!is_array($archivo)) {
                throw new ApiException(
                    'Selecciona un archivo CSV.',
                    400
                );
            }

            $error = $archivo['error'] ?? UPLOAD_ERR_NO_FILE;

            if ($error !== UPLOAD_ERR_OK) {
                $mensaje = match ($error) {
                    UPLOAD_ERR_INI_SIZE, UPLOAD_ERR_FORM_SIZE =>
                    'El archivo supera el limite de 5 MB.',
                    UPLOAD_ERR_NO_FILE =>
                    'Selecciona un archivo CSV.',
                    UPLOAD_ERR_PARTIAL =>
                    'El archivo se recibio incompleto. Seleccionalo nuevamente.',
                    default =>
                    'No se pudo recibir el archivo.',
                };

                $estado = in_array(
                    $error,
                    [UPLOAD_ERR_INI_SIZE, UPLOAD_ERR_FORM_SIZE],
                    true
                ) ? 413 : 400;

                throw new ApiException($mensaje, $estado);
            }

            $temporal = $archivo['tmp_name'] ?? null;
            $nombre = $archivo['name'] ?? null;

            if (!is_string($temporal)
                || !is_string($nombre)
                || !is_uploaded_file($temporal)) {
                throw new ApiException(
                    'El archivo recibido no es valido.',
                    400
                );
            }

            $nombre = basename(str_replace('\\', '/', $nombre));

            if (strtolower(pathinfo($nombre, PATHINFO_EXTENSION)) !== 'csv') {
                throw new ApiException(
                    'Debes seleccionar un archivo con extension .csv.',
                    400
                );
            }

            $tamanio = filesize($temporal);

            if ($tamanio === false || $tamanio > 5 * 1024 * 1024) {
                throw new ApiException(
                    'El archivo supera el limite de 5 MB.',
                    413
                );
            }

            $resultado = $this->modelo->importar($temporal, $nombre);

            $id = filter_var(
                $resultado['importacion']['id'] ?? null,
                FILTER_VALIDATE_INT,
                ['options' => ['min_range' => 1]]
            );

            if ($id === false) {
                throw new ApiException(
                    'No se recibio el identificador del resultado. '
                    . 'Revisa el historial antes de volver a enviar el archivo.',
                    502
                );
            }

            header(
                'Location: ' . enlace('resultado-importacion', ['id' => $id]),
                true,
                303
            );
            exit;

        } catch (ApiException $exception) {
            $this->mostrarFormulario(
                $exception->getMessage(),
                $exception->importacionId(),
                $exception->estado()
            );
        }
    }

    public function historial(): void
    {
        $pagina = $this->entero('page', 0, 0);
        $tamanio = $this->entero('size', 10, 1);

        if ($tamanio > 100) {
            throw new ApiException(
                'El tamanio debe estar entre 1 y 100.',
                400
            );
        }

        render('importaciones/historial', [
            'titulo' => 'Historial de importaciones',
            'resultado' => $this->modelo->listar($pagina, $tamanio),
        ]);
    }

    public function resultado(): void
    {
        $id = $this->entero('id', 0, 1);
        $ver = $_GET['ver'] ?? 'todos';

        if (!is_string($ver)
            || !in_array($ver, ['todos', 'errores'], true)) {
            throw new ApiException(
                'El filtro de resultados no es valido.',
                400
            );
        }

        $detalle = $this->modelo->obtener($id);
        $filas = $detalle['resultados'];

        if ($ver === 'errores') {
            $filas = array_values(array_filter(
                $filas,
                static fn (array $fila): bool => !$fila['aceptado']
            ));
        }

        render('importaciones/resultado', [
            'titulo' => 'Resultado de importacion',
            'importacion' => $detalle['importacion'],
            'filas' => $filas,
            'ver' => $ver,
        ]);
    }

    private function mostrarFormulario(
        ?string $mensaje = null,
        ?int $importacionId = null,
        int $estado = 200
    ): void {
        render('importaciones/nueva', [
            'titulo' => 'Importar CSV',
            'mensaje' => $mensaje,
            'importacionId' => $importacionId,
        ], $estado);
    }

    private function entero(
        string $nombre,
        int $predeterminado,
        int $minimo
    ): int {
        $valor = $_GET[$nombre] ?? (string) $predeterminado;

        if (!is_string($valor)) {
            throw new ApiException(
                'El parametro ' . $nombre . ' no es valido.',
                400
            );
        }

        $numero = filter_var(
            $valor,
            FILTER_VALIDATE_INT,
            ['options' => ['min_range' => $minimo]]
        );

        if ($numero === false) {
            throw new ApiException(
                'El parametro ' . $nombre
                . ' debe ser un entero igual o mayor que ' . $minimo . '.',
                400
            );
        }

        return $numero;
    }
}