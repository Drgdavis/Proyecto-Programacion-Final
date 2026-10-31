CREATE USER IF NOT EXISTS 'importaciones_app'@'localhost'
    IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE, DELETE
    ON importaciones_db.*
    TO 'importaciones_app'@'localhost';

SHOW GRANTS FOR 'importaciones_app'@'localhost';