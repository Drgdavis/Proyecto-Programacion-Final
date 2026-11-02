-- Plantilla: sustituir el marcador en una copia local, fuera de Git.
-- Si ya existe el usuario, CREATE USER IF NOT EXISTS no cambia su clave.
CREATE USER IF NOT EXISTS 'importaciones_app'@'localhost'
    IDENTIFIED BY 'CAMBIAR_AL_CONFIGURAR';

GRANT SELECT, INSERT, UPDATE, DELETE
    ON importaciones_db.*
    TO 'importaciones_app'@'localhost';

SHOW GRANTS FOR 'importaciones_app'@'localhost';
