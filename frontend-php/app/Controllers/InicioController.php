<?php

declare(strict_types=1);

class InicioController
{
    public function __construct(
        private CatalogoModel $catalogo,
        private ImportacionModel $importaciones
    ) {
    }

    public function mostrar(): void
    {
        $productos = $this->catalogo->listar([
            'page' => 0,
            'size' => 1,
        ]);

        $categorias = $this->catalogo->categorias();
        $historial = $this->importaciones->listar(0, 1);

        render('inicio', [
            'titulo' => 'Inicio',
            'totalProductos' => $productos['totalElementos'],
            'totalCategorias' => count($categorias),
            'totalImportaciones' => $historial['totalElementos'],
            'ultimaImportacion' => $historial['contenido'][0] ?? null,
        ]);
    }
}