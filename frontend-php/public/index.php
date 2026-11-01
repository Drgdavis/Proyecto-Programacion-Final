<?php

declare(strict_types=1);

require_once __DIR__ . '/../app/Core/ApiException.php';
require_once __DIR__ . '/../app/Core/ApiClient.php';
require_once __DIR__ . '/../app/Models/CatalogoModel.php';
require_once __DIR__ . '/../app/Controllers/InicioController.php';
require_once __DIR__ . '/../app/Controllers/ProductoController.php';

function e(mixed $valor): string
{
    return htmlspecialchars(
        (string) $valor,
        ENT_QUOTES | ENT_SUBSTITUTE,
        'UTF-8'
    );
}

function enlace(string $ruta, array $parametros = []): string
{
    return '/index.php?' . http_build_query(
            array_merge(['r' => $ruta], $parametros)
        );
}

function render(
    string $vista,
    array $datos = [],
    int $estado = 200
): void {
    http_response_code($estado);
    header('Content-Type: text/html; charset=UTF-8');

    extract($datos, EXTR_SKIP);

    ob_start();
    require __DIR__ . '/../app/Views/' . $vista . '.php';
    $contenido = ob_get_clean();

    require __DIR__ . '/../app/Views/layout.php';
}

try {
    if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
        throw new ApiException('Metodo no permitido.', 405);
    }

    $config = require __DIR__ . '/../config/app.php';

    $api = new ApiClient($config['gateway_url']);
    $catalogo = new CatalogoModel($api);

    $inicioController = new InicioController($catalogo);
    $productoController = new ProductoController($catalogo);

    $ruta = $_GET['r'] ?? 'inicio';

    if (!is_string($ruta)) {
        throw new ApiException('La ruta no es valida.', 400);
    }

    switch ($ruta) {
        case 'inicio':
            $inicioController->mostrar();
            break;

        case 'productos':
            $productoController->listar();
            break;

        case 'producto':
            $productoController->detalle();
            break;

        default:
            throw new ApiException('La pagina solicitada no existe.', 404);
    }

} catch (ApiException $exception) {
    render('error', [
        'titulo' => 'No se pudo completar la solicitud',
        'codigo' => $exception->estado(),
        'mensaje' => $exception->getMessage(),
    ], $exception->estado());

} catch (Throwable $exception) {
    error_log((string) $exception);

    render('error', [
        'titulo' => 'Error',
        'codigo' => 500,
        'mensaje' => 'Ocurrio un error inesperado. Intenta nuevamente.',
    ], 500);
}