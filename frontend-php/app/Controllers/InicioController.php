<?php


declare(strict_types=1);

class InicioController
{
    public function __construct(private CatalogoModel $catalogo)
    {
    }

    public function mostrar(): void
    {
        $productos = $this->catalogo->listar([
            'page' => 0,
            'size' => 1,
        ]);

        $categorias = $this->catalogo->categorias();

        render('inicio', [
            'titulo' => 'Inicio',
            'totalProductos' => $productos['totalElementos'],
            'totalCategorias' => count($categorias),
        ]);
    }
}