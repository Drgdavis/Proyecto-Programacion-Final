-- Plantilla: sustituir el marcador en una copia local, fuera de Git.
-- Si ya existe el usuario, CREATE USER IF NOT EXISTS no cambia su clave.
CREATE USER IF NOT EXISTS 'catalogo_app'@'localhost'
    IDENTIFIED BY 'CAMBIAR_AL_CONFIGURAR';

GRANT SELECT, INSERT, UPDATE, DELETE
    ON catalogo_db.*
    TO 'catalogo_app'@'localhost';

SHOW GRANTS FOR 'catalogo_app'@'localhost';
