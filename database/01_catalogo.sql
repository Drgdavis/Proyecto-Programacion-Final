-- Base de datos del microservicio de catalogo
CREATE DATABASE IF NOT EXISTS catalogo_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE catalogo_db;

-- Categorias disponibles para los productos
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_categorias_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

-- Productos registrados en el catalogo
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    categoria_id BIGINT NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    existencia INT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_productos_codigo UNIQUE (codigo),

    CONSTRAINT fk_productos_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categorias (id),

    CONSTRAINT chk_productos_precio
        CHECK (precio > 0),

    CONSTRAINT chk_productos_existencia
        CHECK (existencia >= 0)
) ENGINE = InnoDB;

-- Categorias iniciales; se agregan solo si no existen
INSERT INTO categorias (nombre)
SELECT 'Perifericos'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre = 'Perifericos'
);

INSERT INTO categorias (nombre)
SELECT 'Monitores'
WHERE NOT EXISTS (
    SELECT 1 FROM categorias WHERE nombre = 'Monitores'
);

-- Comprobacion
SHOW TABLES;
SELECT id, nombre FROM categorias;