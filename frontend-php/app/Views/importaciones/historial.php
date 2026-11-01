<section class="encabezado">
    <p class="etiqueta">IMPORTACIONES</p>
    <h1>Historial</h1>
    <p class="secundario">
        Consulta los archivos procesados y sus resultados.
    </p>

    <a class="boton" href="<?= e(enlace('importar')) ?>">
        Nueva importación
    </a>
</section>

<section class="tarjeta">
    <div class="barra">
        <h2>Importaciones registradas</h2>
        <span class="secundario">
            <?= e($resultado['totalElementos']) ?> intentos
        </span>
    </div>

    <?php if ($resultado['contenido'] === []): ?>
        <p class="vacio">Todavía no hay importaciones registradas.</p>
    <?php else: ?>
        <div class="tabla-contenedor">
            <table>
                <caption class="solo-lectores">
                    Historial de importaciones
                </caption>
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Archivo</th>
                    <th scope="col">Fecha</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Total</th>
                    <th scope="col">Aceptados</th>
                    <th scope="col">Rechazados</th>
                    <th scope="col">Resultado</th>
                </tr>
                </thead>
                <tbody>
                <?php foreach ($resultado['contenido'] as $importacion): ?>
                    <tr>
                        <td><?= e($importacion['id']) ?></td>
                        <td><?= e($importacion['archivo']) ?></td>
                        <td>
                            <?= e(str_replace(
                                'T',
                                ' ',
                                $importacion['fecha']
                            )) ?>
                        </td>
                        <td><?= e($importacion['estado']) ?></td>
                        <td><?= e($importacion['total']) ?></td>
                        <td><?= e($importacion['aceptados']) ?></td>
                        <td><?= e($importacion['rechazados']) ?></td>
                        <td>
                            <a href="<?= e(enlace('resultado-importacion', [
                                'id' => $importacion['id'],
                            ])) ?>">
                                Ver resultado
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
    $tamanio = (int) $resultado['tamanio'];
    ?>

    <nav class="paginacion" aria-label="Paginación del historial">
        <?php if ($paginaActual > 0): ?>
            <a class="boton secundario" href="<?= e(enlace('historial', [
                'page' => $paginaActual - 1,
                'size' => $tamanio,
            ])) ?>">
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
            <a class="boton secundario" href="<?= e(enlace('historial', [
                'page' => $paginaActual + 1,
                'size' => $tamanio,
            ])) ?>">
                Siguiente
            </a>
        <?php endif; ?>
    </nav>
</section>