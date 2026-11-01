<?php

declare(strict_types=1);

class ImportacionModel
{
    public function __construct(private ApiClient $api)
    {
    }

    public function listar(int $pagina, int $tamanio): array
    {
        return $this->api->get('/api/importaciones', [
            'page' => $pagina,
            'size' => $tamanio,
        ]);
    }

    public function obtener(int $id): array
    {
        return $this->api->get('/api/importaciones/' . $id);
    }

    public function importar(
        string $temporal,
        string $nombre
    ): array {
        return $this->api->subirArchivo(
            '/api/importaciones',
            $temporal,
            $nombre
        );
    }
}