<section class="encabezado">
    <p class="etiqueta">IMPORTACIONES</p>
    <h1>Importar productos</h1>
    <p class="secundario">
        Carga de productos mediante archivos CSV.
    </p>
</section>

<?php if ($mensaje !== null): ?>
    <section class="tarjeta error" role="alert">
        <h2>No se pudo completar la carga</h2>
        <p><?= e($mensaje) ?></p>

        <?php if ($importacionId !== null): ?>
            <a href="<?= e(enlace('resultado-importacion', [
                    'id' => $importacionId,
            ])) ?>">
                Consultar el intento <?= e($importacionId) ?>
            </a>
        <?php endif; ?>

        <p>
            Los intentos anteriores están disponibles en
            <a href="<?= e(enlace('historial')) ?>">el historial</a>.
        </p>
    </section>
<?php endif; ?>

<section class="tarjeta">
    <h2>Formato del archivo</h2>

    <p>
        CSV en UTF-8, separado por comas. El precio utiliza punto decimal
        y la existencia debe ser un entero.
    </p>

    <p>Encabezados, en este orden:</p>

    <pre class="ejemplo">codigo,nombre,categoria,precio,existencia</pre>

    <p>Ejemplo de una fila:</p>

    <pre class="ejemplo">NUEVO001,Teclado USB,Perifericos,250.00,10</pre>

    <div class="acciones">
        <a
                class="boton secundario"
                href="/plantilla_productos.csv"
                download="plantilla_productos.csv"
        >
            Descargar plantilla CSV
        </a>
    </div>

    <p class="secundario">
        La plantilla contiene dos productos de ejemplo.
        Los datos de ejemplo deben reemplazarse por códigos que no estén registrados.
        El archivo debe conservar los encabezados, la codificación UTF-8
        y el separador coma.
    </p>

    <p class="secundario">
        Tamaño máximo: 5 MB. Las categorías deben existir en el catálogo.
    </p>
</section>

<form
        method="post"
        action="<?= e(enlace('procesar-importacion')) ?>"
        enctype="multipart/form-data"
        class="tarjeta"
>
    <input type="hidden" name="MAX_FILE_SIZE" value="5242880">

    <div class="campo">
        <label for="archivo">Archivo CSV</label>

        <input
                type="file"
                id="archivo"
                name="archivo"
                accept=".csv,text/csv"
                aria-describedby="ayuda-archivo"
                required
        >

        <p id="ayuda-archivo" class="secundario">
            Archivo CSV con los productos de la importación.
        </p>
    </div>

    <div class="acciones">
        <button type="submit" class="boton">
            Importar productos
        </button>

        <a class="boton secundario" href="<?= e(enlace('historial')) ?>">
            Ver historial
        </a>
    </div>
</form>