<section class="encabezado">
    <p class="etiqueta">RESUMEN</p>
    <h1>Gestión de productos</h1>
    <p class="secundario">
        Importación de productos, resultados y consulta del catálogo.
    </p>
</section>

<section class="tarjetas" aria-label="Resumen del sistema">
    <article class="tarjeta indicador">
        <span>Productos registrados</span>
        <strong><?= e($totalProductos) ?></strong>
    </article>

    <article class="tarjeta indicador">
        <span>Categorías disponibles</span>
        <strong><?= e($totalCategorias) ?></strong>
    </article>

    <article class="tarjeta indicador">
        <span>Importaciones registradas</span>
        <strong><?= e($totalImportaciones) ?></strong>
    </article>
</section>

<section class="tarjeta">
    <h2>Importa un archivo de productos</h2>
    <p>
        Recibirás un resumen y el resultado de cada fila.
        Los registros rechazados incluirán su motivo.
    </p>

    <div class="acciones">
        <a class="boton" href="<?= e(enlace('importar')) ?>">
            Importar CSV
        </a>

        <a class="boton secundario" href="<?= e(enlace('productos')) ?>">
            Consultar productos
        </a>
    </div>
</section>

<?php if ($ultimaImportacion !== null): ?>
    <section class="tarjeta">
        <h2>Última importación</h2>
        <p>
            <strong><?= e($ultimaImportacion['archivo']) ?></strong>
        </p>
        <p class="secundario">
            <?= e($ultimaImportacion['estado']) ?>
            · <?= e($ultimaImportacion['aceptados']) ?> aceptados
            · <?= e($ultimaImportacion['rechazados']) ?> rechazados
        </p>

        <a href="<?= e(enlace('resultado-importacion', [
            'id' => $ultimaImportacion['id'],
        ])) ?>">
            Consultar resultado
        </a>
    </section>
<?php endif; ?>