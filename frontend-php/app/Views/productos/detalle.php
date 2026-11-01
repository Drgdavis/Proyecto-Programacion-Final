<section class="encabezado">
    <p class="etiqueta">DETALLE DE PRODUCTO</p>
    <h1><?= e($producto['nombre']) ?></h1>
    <p class="secundario">
        Código <?= e($producto['codigo']) ?>
    </p>
</section>

<article class="tarjeta">
    <dl class="detalle">
        <div>
            <dt>Identificador</dt>
            <dd><?= e($producto['id']) ?></dd>
        </div>

        <div>
            <dt>Código</dt>
            <dd><?= e($producto['codigo']) ?></dd>
        </div>

        <div>
            <dt>Categoría</dt>
            <dd><?= e($producto['categoria']) ?></dd>
        </div>

        <div>
            <dt>Precio</dt>
            <dd>
                <?= e(number_format(
                    (float) $producto['precio'],
                    2,
                    '.',
                    ','
                )) ?>
            </dd>
        </div>

        <div>
            <dt>Existencia</dt>
            <dd><?= e($producto['existencia']) ?></dd>
        </div>
    </dl>

    <a class="boton secundario" href="<?= e(enlace('productos')) ?>">
        Volver a productos
    </a>
</article>