<section class="encabezado">
    <p class="etiqueta">CATÁLOGO</p>
    <h1>Gestión de productos</h1>
    <p class="secundario">
        Consulta los productos registrados y encuentra sus detalles.
    </p>
</section>

<section class="tarjetas" aria-label="Resumen del catálogo">
    <article class="tarjeta indicador">
        <span>Productos registrados</span>
        <strong><?= e($totalProductos) ?></strong>
    </article>

    <article class="tarjeta indicador">
        <span>Categorías disponibles</span>
        <strong><?= e($totalCategorias) ?></strong>
    </article>
</section>

<section class="tarjeta">
    <h2>Consulta el catálogo</h2>
    <p>
        Busca por código, nombre o categoría y revisa el precio
        y la existencia de cada producto.
    </p>

    <a class="boton" href="<?= e(enlace('productos')) ?>">
        Ver productos
    </a>
</section>