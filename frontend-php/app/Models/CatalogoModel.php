<?php


declare(strict_types=1);

class CatalogoModel
{
    public function __construct(private ApiClient $api)
    {
    }

    public function listar(array $parametros): array
    {
        return $this->api->get('/api/productos', $parametros);
    }

    public function categorias(): array
    {
        return $this->api->get('/api/categorias');
    }

    public function obtener(int $id): array
    {
        return $this->api->get('/api/productos/' . $id);
    }
}