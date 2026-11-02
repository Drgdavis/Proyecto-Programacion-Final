# Configuración de MySQL

## Primera instalación

Se utiliza MySQL 8.4. En Workbench abre una conexión administrativa y ejecuta, en orden:

1. [01_catalogo.sql](01_catalogo.sql).
2. [02_importaciones.sql](02_importaciones.sql).
3. Copias locales de las plantillas de usuarios, sustituyendo `CAMBIAR_AL_CONFIGURAR` por claves propias.

La base del catálogo contiene categorías y productos. La base de importaciones contiene intentos y resultados. Los scripts no borran datos y agregan las categorías iniciales solamente si no existen.

## Usuarios

- `catalogo_app@localhost`: SELECT, INSERT, UPDATE y DELETE en catalogo_db.
- `importaciones_app@localhost`: los mismos permisos, exclusivamente en importaciones_db.
- La creación y modificación del esquema se ejecuta como administrador.
- La aplicación no necesita permisos CREATE, DROP ni ALTER.

`CREATE USER IF NOT EXISTS` no actualiza una contraseña existente. Para cambiarla, ejecuta como administrador, sustituyendo el marcador:

```sql
ALTER USER 'catalogo_app'@'localhost'
    IDENTIFIED BY 'NUEVA_CLAVE_LOCAL';

ALTER USER 'importaciones_app'@'localhost'
    IDENTIFIED BY 'OTRA_CLAVE_LOCAL';
```

No guardes las claves sustituidas en Git. Si utilizaste la clave concreta que apareció anteriormente en una plantilla del proyecto, cámbiala: sustituir el archivo actual no la elimina de commits antiguos.

## Instalaciones anteriores

La columna `importaciones.mensaje` se agregó durante el desarrollo. Si tu tabla fue creada antes de esa mejora, comprueba:

```sql
SHOW COLUMNS FROM importaciones_db.importaciones LIKE 'mensaje';
```

Si no existe, ejecuta una vez:

```sql
ALTER TABLE importaciones_db.importaciones ADD COLUMN mensaje TEXT;
```

El script de creación ya incluye la columna para instalaciones nuevas. CREATE TABLE IF NOT EXISTS no modifica una tabla existente.

## Comprobaciones

```sql
SELECT id, nombre FROM catalogo_db.categorias;
SHOW TABLES FROM catalogo_db;
SHOW TABLES FROM importaciones_db;
SHOW GRANTS FOR 'catalogo_app'@'localhost';
SHOW GRANTS FOR 'importaciones_app'@'localhost';
```

No ejecutes los tests Maven contra estas bases. El perfil de tests utiliza H2 efímero y genera su propio esquema.

## Copia de respaldo

Utiliza Data Export de Workbench y selecciona ambas bases, estructura y datos. Guarda la copia fuera de Git si contiene información o claves locales. Para una demostración basta con los scripts y CSV del repositorio; no es obligatorio entregar el historial personal completo.
