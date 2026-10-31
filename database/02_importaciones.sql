CREATE DATABASE IF NOT EXISTS importaciones_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE importaciones_db;

CREATE TABLE IF NOT EXISTS importaciones (
    id BIGINT NOT NULL AUTO_INCREMENT,
    archivo VARCHAR(255) NOT NULL,
    fecha DATETIME(6) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    total INT NOT NULL DEFAULT 0,
    aceptados INT NOT NULL DEFAULT 0,
    rechazados INT NOT NULL DEFAULT 0,
    mensaje TEXT,

    PRIMARY KEY (id),

    CONSTRAINT chk_importaciones_contadores CHECK (
        total >= 0
        AND aceptados >= 0
        AND rechazados >= 0
        AND aceptados + rechazados <= total
    )
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS resultados_importacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    importacion_id BIGINT NOT NULL,
    fila INT NOT NULL,
    codigo VARCHAR(255),
    aceptado BOOLEAN NOT NULL,
    producto_id BIGINT,
    motivo TEXT,

    PRIMARY KEY (id),

    CONSTRAINT fk_resultados_importacion
        FOREIGN KEY (importacion_id)
        REFERENCES importaciones(id),

    CONSTRAINT uk_resultados_importacion_fila
        UNIQUE (importacion_id, fila),

    CONSTRAINT chk_resultados_fila CHECK (fila >= 2)
) ENGINE = InnoDB;

SHOW TABLES;