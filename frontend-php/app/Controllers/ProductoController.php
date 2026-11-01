<?php

declare(strict_types=1);

class ProductoController
{
    public function __construct(private CatalogoModel $catalogo)
    {
    }

    public function listar(): void
    {
        $pagina = $this->entero('page', 0, 0);
        $tamanio = $this->entero('size', 10, 1);

        if ($tamanio > 100) {
            throw new ApiException(
                'El tamanio debe estar entre 1 y 100.',
                400
            );
        }

        $categoria = $this->texto('categoriaId');

        if ($categoria !== '') {
            $this->entero('categoriaId', 1, 1);
        }

        $filtros = [
            'codigo' => $this->texto('codigo'),
            'nombre' => $this->texto('nombre'),
            'categoriaId' => $categoria,
            'size' => $tamanio,
        ];

        $parametros = array_filter(
            $filtros,
            static fn ($valor) => $valor !== ''
        );

        $parametros['page'] = $pagina;

        $resultado = $this->catalogo->listar($parametros);
        $categorias = $this->catalogo->categorias();

        render('productos/index', [
            'titulo' => 'Productos',
            'resultado' => $resultado,
            'categorias' => $categorias,
            'filtros' => $filtros,
        ]);
    }

    public function detalle(): void
    {
        $id = $this->entero('id', 0, 1);
        $producto = $this->catalogo->obtener($id);

        render('productos/detalle', [
            'titulo' => 'Detalle de producto',
            'producto' => $producto,
        ]);
    }

    private function texto(string $nombre): string
    {
        $valor = $_GET[$nombre] ?? '';

        if (!is_string($valor)) {
            throw new ApiException(
                'El parametro ' . $nombre . ' tiene un formato incorrecto.',
                400
            );
        }

        return trim($valor);
    }

    private function entero(
        string $nombre,
        int $predeterminado,
        int $minimo
    ): int {
        $valor = $this->texto($nombre);

        if ($valor === '') {
            $valor = (string) $predeterminado;
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