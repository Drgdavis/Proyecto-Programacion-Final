<section class="encabezado">
    <p class="etiqueta">CATÁLOGO</p>
    <h1>Productos</h1>
    <p class="secundario">Busca y consulta los productos registrados.</p>
</section>

<form method="get" action="/index.php" class="tarjeta filtros">
    <input type="hidden" name="r" value="productos">

    <div class="campo">
        <label for="codigo">Código</label>
        <input
            id="codigo"
            name="codigo"
            value="<?= e($filtros['codigo']) ?>"
            placeholder="Ej. CSV001"
        >
    </div>

    <div class="campo">
        <label for="nombre">Nombre</label>
        <input
            id="nombre"
            name="nombre"
            value="<?= e($filtros['nombre']) ?>"
            placeholder="Ej. Teclado"
        >
    </div>

    <div class="campo">
        <label for="categoriaId">Categoría</label>
        <select id="categoriaId" name="categoriaId">
            <option value="">Todas</option>

            <?php foreach ($categorias as $categoria): ?>
                <option
                    value="<?= e($categoria['id']) ?>"
                    <?= (string) $filtros['categoriaId']
                    === (string) $categoria['id']
                        ? 'selected' : '' ?>
                >
                    <?= e($categoria['nombre']) ?>
                </option>
            <?php endforeach; ?>
        </select>
    </div>

    <div class="campo campo-corto">
        <label for="size">Por página</label>
        <input
            type="number"
            id="size"
            name="size"
            min="1"
            max="100"
            value="<?= e($filtros['size']) ?>"
            required
        >
    </div>

    <div class="acciones">
        <button type="submit" class="boton">Buscar</button>

        <a class="boton secundario" href="<?= e(enlace('productos')) ?>">
            Limpiar
        </a>
    </div>
</form>

<section class="tarjeta">
    <div class="barra">
        <h2>Resultados</h2>
        <span class="secundario">
            <?= e($resultado['totalElementos']) ?> productos encontrados
        </span>
    </div>

    <?php if ($resultado['contenido'] === []): ?>
        <p class="vacio">No hay productos para esta búsqueda.</p>
    <?php else: ?>
        <div class="tabla-contenedor">
            <table>
                <caption class="solo-lectores">
                    Productos encontrados
                </caption>
                <thead>
                <tr>
                    <th scope="col">Código</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Categoría</th>
                    <th scope="col">Precio</th>
                    <th scope="col">Existencia</th>
                    <th scope="col">Detalle</th>
                </tr>
                </thead>
                <tbody>
                <?php foreach ($resultado['contenido'] as $producto): ?>
                    <tr>
                        <td><?= e($producto['codigo']) ?></td>
                        <td><?= e($producto['nombre']) ?></td>
                        <td><?= e($producto['categoria']) ?></td>
                        <td>
                            <?= e(number_format(
                                (float) $producto['precio'],
                                2,
                                '.',
                                ','
                            )) ?>
                        </td>
                        <td><?= e($producto['existencia']) ?></td>
                        <td>
                            <a href="<?= e(enlace('producto', [
                                'id' => $producto['id'],
                            ])) ?>">
                                Ver detalle
                            </a>
                        </td>
                    </tr>
                <?php endforeach; ?>
                </tbody>
            </table>
        </div>
    <?php endif; ?>

    <?php
    $paginaActual = (int) $resultado['pagina'];
    $totalPaginas = (int) $resultado['totalPaginas'];
    ?>

    <nav class="paginacion" aria-label="Paginación de productos">
        <?php if ($paginaActual > 0): ?>
            <a class="boton secundario" href="<?= e(enlace(
                'productos',
                array_merge($filtros, ['page' => $paginaActual - 1])
            )) ?>">
                Anterior
            </a>
        <?php endif; ?>

        <span>
            <?php if ($totalPaginas > 0): ?>
                Página <?= e($paginaActual + 1) ?>
                de <?= e($totalPaginas) ?>
            <?php else: ?>
                Sin resultados
            <?php endif; ?>
        </span>

        <?php if ($paginaActual + 1 < $totalPaginas): ?>
            <a class="boton secundario" href="<?= e(enlace(
                'productos',
                array_merge($filtros, ['page' => $paginaActual + 1])
            )) ?>">
                Siguiente
            </a>
        <?php endif; ?>
    </nav>
</section>