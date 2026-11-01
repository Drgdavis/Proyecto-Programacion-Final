<section class="tarjeta error">
    <p class="etiqueta">ERROR <?= e($codigo) ?></p>
    <h1>No se pudo completar la solicitud</h1>
    <p><?= e($mensaje) ?></p>

    <a class="boton" href="<?= e(enlace('inicio')) ?>">
        Volver al inicio
    </a>
</section>