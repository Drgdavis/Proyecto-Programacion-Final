<section class="encabezado">
    <p class="etiqueta">RESULTADO DE IMPORTACIÓN</p>
    <h1><?= e($importacion['archivo']) ?></h1>

    <p class="secundario">
        Importación <?= e($importacion['id']) ?>
        · <?= e(str_replace('T', ' ', $importacion['fecha'])) ?>
    </p>

    <span class="estado"><?= e($importacion['estado']) ?></span>
</section>

<?php if (!empty($importacion['mensaje'])): ?>
    <section class="tarjeta error" role="alert">
        <h2>Información del procesamiento</h2>
        <p><?= e($importacion['mensaje']) ?></p>
    </section>
<?php endif; ?>

<?php if ($importacion['estado'] === 'FALLIDA'): ?>
    <p class="secundario">
        El procesamiento no terminó correctamente.
        Los contadores muestran únicamente resultados confirmados.
    </p>
<?php endif; ?>

<section class="tarjetas" aria-label="Resumen de resultados">
    <article class="tarjeta indicador">
        <span>Total de filas de productos</span>
        <strong><?= e($importacion['total']) ?></strong>
    </article>

    <article class="tarjeta indicador">
        <span>Aceptados</span>
        <strong class="texto-exito">
            <?= e($importacion['aceptados']) ?>
        </strong>
    </article>

    <article class="tarjeta indicador">
        <span>Rechazados</span>
        <strong class="texto-error">
            <?= e($importacion['rechazados']) ?>
        </strong>
    </article>
</section>

<section class="tarjeta">
    <div class="barra">
        <h2>Resultados por fila</h2>

        <div class="acciones">
            <a class="boton secundario" href="<?= e(enlace(
                'resultado-importacion',
                ['id' => $importacion['id']]
            )) ?>">
                Todas
            </a>

            <a class="boton secundario" href="<?= e(enlace(
                'resultado-importacion',
                ['id' => $importacion['id'], 'ver' => 'errores']
            )) ?>">
                Solo errores
            </a>
        </div>
    </div>

    <p class="secundario">
        <?= $ver === 'errores'
            ? 'Mostrando únicamente filas rechazadas.'
            : 'Mostrando todos los resultados disponibles.' ?>
    </p>

    <?php if ($filas === []): ?>
        <p class="vacio">
            <?= $ver === 'errores'
                ? 'No hay filas rechazadas para mostrar.'
                : 'No hay resultados por fila disponibles.' ?>
        </p>
    <?php else: ?>
        <div class="tabla-contenedor">
            <table>
                <caption class="solo-lectores">
                    Resultado de cada fila del archivo
                </caption>
                <thead>
                <tr>
                    <th scope="col">Fila CSV</th>
                    <th scope="col">Código</th>
                    <th scope="col">Resultado</th>
                    <th scope="col">Producto</th>
                    <th scope="col">Motivo</th>
                </tr>
                </thead>
                <tbody>
                <?php foreach ($filas as $fila): ?>
                    <tr>
                        <td><?= e($fila['fila']) ?></td>
                        <td><?= e($fila['codigo']) ?></td>
                        <td>
                            <span class="<?= $fila['aceptado']
                                ? 'texto-exito' : 'texto-error' ?>">
                                <?= $fila['aceptado']
                                    ? 'Aceptado' : 'Rechazado' ?>
                            </span>
                        </td>
                        <td>
                            <?php if ($fila['aceptado']
                                && $fila['productoId'] !== null): ?>
                                <a href="<?= e(enlace('producto', [
                                    'id' => $fila['productoId'],
                                ])) ?>">
                                    Ver producto <?= e($fila['productoId']) ?>
                                </a>
                            <?php else: ?>
                                —
                            <?php endif; ?>
                        </td>
                        <td><?= e($fila['motivo'] ?? '—') ?></td>
                    </tr>
                <?php endforeach; ?>
                </tbody>
            </table>
        </div>
    <?php endif; ?>
</section>

<div class="acciones">
    <a class="boton" href="<?= e(enlace('importar')) ?>">
        Nueva importación
    </a>

    <a class="boton secundario" href="<?= e(enlace('historial')) ?>">
        Volver al historial
    </a>
</div>