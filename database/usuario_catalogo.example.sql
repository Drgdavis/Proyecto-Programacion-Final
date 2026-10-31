CREATE USER 'catalogo_app'@'localhost'
IDENTIFIED BY 'CAMBIAR_AL_CONFIGURAR';

GRANT SELECT, INSERT, UPDATE, DELETE
ON catalogo_db.*
TO 'catalogo_app'@'localhost';

SHOW GRANTS FOR 'catalogo_app'@'localhost';