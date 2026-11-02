# Catálogo

Servicio de productos y categorías, puerto8081, nombre lógico catalogo-service. Es el único componente que escribe catalogo_db.

## Ejecutar

Crear el esquema con ../database/01_catalogo.sql y configurar el usuario mediante la plantilla. Desde raíz: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Iniciar-Servicio.ps1 -Servicio Catalogo`. Solicita CATALOGO_DB_PASSWORD si falta. Iniciar Eureka primero.

## Contrato

GET /api/categorias; GET /api/productos con codigo,nombre,categoriaId,page,size; GET /api/productos/{id}; POST /api/productos/lote. Swagger: http://localhost:8081/swagger-ui.html.

Lote acepta resultados parciales; itemId correlaciona resultados. La categoría debe existir, el código ser único, precio positivo con dos decimales como máximo y existencia entera no negativa.

## Código y pruebas

Controllers delegan en servicios; repositorios JPA consultan entidades. ProductoRepository comparte filtros en select/count. RegistroProductoService usa REQUIRES_NEW para cada producto a través de otro bean. UNIQUE protege frente a concurrencia.

` .\mvnw.cmd -B verify` ejecuta cinco pruebas con H2 de test. El arranque normal valida el esquema MySQL; no utiliza H2. Logs en logs/catalogo-service.log.

Ver [API](../docs/API.md), [arquitectura](../docs/ARQUITECTURA.md) y [pruebas](../docs/PRUEBAS_INTEGRACION.md).
