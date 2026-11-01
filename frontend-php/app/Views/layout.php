<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= e($titulo ?? 'Gestion de productos') ?></title>
    <link rel="stylesheet" href="/assets/app.css">
</head>
<body>
<header class="cabecera">
    <div class="contenedor barra">
        <a class="marca" href="<?= e(enlace('inicio')) ?>">
            Gestión de productos
        </a>

        <nav aria-label="Navegación principal">
            <a href="<?= e(enlace('inicio')) ?>">Inicio</a>
            <a href="<?= e(enlace('productos')) ?>">Productos</a>
        </nav>
    </div>
</header>

<main class="contenedor principal">
    <?= $contenido ?>
</main>

<footer class="contenedor pie">
    Proyecto final · Programación IV
</footer>
</body>
</html>