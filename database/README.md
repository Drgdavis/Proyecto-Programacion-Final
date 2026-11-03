# Configuración de MySQL

## Primera instalación

Se utiliza MySQL 8.4. En Workbench, los scripts se ejecutan mediante una conexión administrativa, en este orden:

1. [01_catalogo.sql](01_catalogo.sql).
2. [02_importaciones.sql](02_importaciones.sql).
3. Copias locales de las plantillas de usuarios, sustituyendo `CAMBIAR_AL_CONFIGURAR` por claves propias.

La base del catálogo contiene categorías y productos. La base de importaciones contiene intentos y resultados. Los scripts no borran datos y agregan las categorías iniciales solamente si no existen.

## Usuarios

- `catalogo_app@localhost`: SELECT, INSERT, UPDATE y DELETE en catalogo_db.
- `importaciones_app@localhost`: los mismos permisos, exclusivamente en importaciones_db.
- La creación y modificación del esquema se ejecuta como administrador.
- La aplicación no necesita permisos CREATE, DROP ni ALTER.

`CREATE USER IF NOT EXISTS` no actualiza una contraseña existente. Para cambiarla, ejecutar como administrador con el marcador sustituido por la clave local:

```sql
ALTER USER 'catalogo_app'@'localhost'
    IDENTIFIED BY 'NUEVA_CLAVE_LOCAL';

ALTER USER 'importaciones_app'@'localhost'
    IDENTIFIED BY 'OTRA_CLAVE_LOCAL';
```

Las claves locales deben permanecer fuera de Git. Las claves expuestas en versiones anteriores requieren rotación, ya que reemplazar una plantilla no modifica el historial.

## Instalaciones anteriores

La columna `importaciones.mensaje` se agregó durante el desarrollo. Para tablas creadas antes de esa mejora, comprobar:

```sql
SHOW COLUMNS FROM importaciones_db.importaciones LIKE 'mensaje';
```

Si la columna no existe, ejecutar una vez:

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

Los tests Maven se ejecutan sobre una base aislada, no sobre estas bases. El perfil de tests utiliza H2 efímero y genera su propio esquema.

## Copia de respaldo

El respaldo se realiza mediante Data Export de Workbench, incluyendo estructura y datos de ambas bases. Las copias con información o claves locales deben permanecer fuera de Git. Para una demostración basta con los scripts y CSV del repositorio; no es obligatorio entregar el historial personal completo.
