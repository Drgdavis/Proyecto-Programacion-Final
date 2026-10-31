# Avances y continuidad — Proyecto de Programación IV

Este archivo es la bitácora del proyecto. Se actualiza al cerrar cada avance y antes de terminar una sesión. Complementa la guía completa de implementación.

## 1. Resumen para retomar

| Dato | Estado |
|---|---|
| Última sesión documentada | Viernes 30 de octubre de 2026, horario de Ciudad de México. |
| Entrega de trabajo | Martes 3 de noviembre de 2026, según la aclaración del estudiante. El PDF original indica el lunes 2. |
| Avance global estimado | Aproximadamente 10 %. Es una estimación de trabajo, no una calificación. |
| Repositorio | https://github.com/Drgdavis/Proyecto-Programacion-Final |
| Carpeta local | `C:\Users\davis\IdeaProjects\Proyecto-Programacion-Final` |
| Rama utilizada | `main`. |
| Último resultado comprobado | GET /api/categorias responde HTTP 200 con Monitores (2) y Perifericos (1), leidos de MySQL. |
| Próxima tarea | Implementar consulta de productos con filtros, paginacion y detalle individual. |
| Pendiente de confirmar | Resultado HTTP y contenido real de categorías. Los archivos nuevos de esta consulta, la bitácora y la plantilla de usuario aún requieren revisión y commit. |

**Al volver:** comprobar que MySQL esté ejecutándose, proporcionar la contraseña en la terminal, arrancar catálogo y abrir `http://localhost:8081/api/categorias`. La compilación ya está comprobada; falta verificar el resultado HTTP y la lectura de la base.

## 2. Contexto de aprendizaje y forma de trabajar

- Java es el lenguaje que más conoce el estudiante.
- Spring Boot aplicado a API, PHP MVC y MySQL se están trabajando paso a paso.
- Eureka y Gateway aparecieron en la presentación de la última clase, sin explicación ni práctica. Hay que tratarlos como temas nuevos y comenzar con una integración pequeña.
- El estudiante prefiere concentrar el estudio profundo y la defensa después de completar la implementación.
- Durante el desarrollo se mantienen explicaciones breves sobre qué recibe cada parte, qué hace y qué devuelve.
- Las instrucciones deben señalar claramente dónde actuar: IntelliJ, editor SQL de Workbench, ventana de guardar o terminal PowerShell.
- En la terminal se ejecutan comandos completos, uno por uno. Las rutas que solo describen ubicaciones no son comandos.
- La defensa se practicará como una entrevista exigente: una pregunta a la vez, explicación sobre código real, seguimiento de solicitudes y cambios prácticos.

## 3. Avances comprobados

### Entorno y repositorio

- [x] Crear el repositorio en GitHub.
- [x] Clonarlo y abrirlo en IntelliJ.
- [x] Configurar la identidad de Git para el repositorio.
- [x] Crear `README.md` y `.gitignore`.
- [x] Retirar `.idea` del seguimiento de Git.
- [x] Instalar/seleccionar Eclipse Temurin JDK 21.
- [x] Hacer funcionar `java -version` desde PowerShell.
- [x] Configurar `JAVA_HOME` y agregar el JDK al Path del usuario.
- [x] Confirmar un push del proyecto inicial de catálogo.

### Microservicio de catálogo

- [x] Generar `catalogo-service` con Spring Initializr.
- [x] Corregir la extracción para que exista un solo nivel `catalogo-service`.
- [x] Arrancar la aplicación inicial en el puerto 8080.
- [x] Agregar Spring Data JPA y MySQL Connector/J al `pom.xml`.
- [x] Resolver dependencias con Maven: resultado `BUILD SUCCESS`.
- [x] Configurar datasource, usuario de aplicación y contraseña desde variable de entorno.
- [x] Cambiar el puerto del catálogo a 8081.
- [x] Arrancar con JPA inicializado y mensaje `Started CatalogoServiceApplication`.
- [ ] Ajustar el nivel de lenguaje del IDE a Java 21 normal o `SDK default`: se confirmó JDK_21_PREVIEW el 30 de octubre. La compilación Maven ya utiliza Java 21 normal.
- [x] Implementar entidad, repositorio, DTO, servicio y controlador para `GET /api/categorias`.
- [x] Compilar el proyecto con la consulta de categorías: `BUILD SUCCESS` el 30 de octubre.
- [x] Demostrar la lectura real mediante HTTP: categorias verificadas con HTTP 200.

### MySQL y esquema del catálogo

- [x] Instalar y configurar MySQL Community Server 8.4.13.
- [x] Instalar MySQL Workbench 26.10.0.
- [x] Probar conexión administrativa a `127.0.0.1:3306` con éxito.
- [x] Preparar y guardar `database/01_catalogo.sql`, según lo confirmado durante la sesión.
- [x] Ejecutar el script.
- [x] Ver las tablas `categorias` y `productos` en `SHOW TABLES`.
- [x] Incluir en el script las categorías iniciales `Perifericos` y `Monitores`.
- [x] Crear `catalogo_app` con permisos SELECT, INSERT, UPDATE y DELETE sobre `catalogo_db`.
- [x] Verificar esos permisos mediante `SHOW GRANTS`.
- [x] Confirmar las dos categorias mediante el endpoint y la consulta SQL directa.

El arranque con JPA es un avance de configuración. La siguiente comprobación funcional será leer categorías desde la base y devolverlas mediante HTTP.

## 4. Configuración acordada

| Elemento | Valor |
|---|---|
| Java | Eclipse Temurin 21.0.12.1. |
| Spring Boot | 4.0.9. |
| Construcción | Maven con Wrapper: `mvnw.cmd`. |
| Group | `com.prograiv`. |
| Artifact | `catalogo-service`. |
| Paquete base | `com.prograiv.catalogo`. |
| Nombre del servicio | `catalogo-service`. |
| Puerto actual de catálogo | 8081. |
| Host y puerto de MySQL | `localhost:3306` en Spring Boot. |
| Base de catálogo | `catalogo_db`. |
| Usuario de catálogo | `catalogo_app` asociado a `localhost`. |
| Variable de contraseña | `CATALOGO_DB_PASSWORD`. |
| Gestión de esquema | `spring.jpa.hibernate.ddl-auto=validate`. |
| Gateway | Pendiente; puerto 8080 previsto. |
| Eureka e importaciones | Pendientes de crear y configurar. |

Dependencias actuales de catálogo:

- `spring-boot-starter-webmvc`.
- `spring-boot-starter-validation`.
- `spring-boot-starter-data-jpa`.
- `mysql-connector-j`, con alcance runtime.
- Dependencias de pruebas Web MVC y Validation generadas inicialmente.

Contenido acordado de `application.properties`:

```properties
spring.application.name=catalogo-service
server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3306/catalogo_db
spring.datasource.username=catalogo_app
spring.datasource.password=${CATALOGO_DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
```

Las contraseñas reales se conservan localmente. Los archivos compartidos utilizan variables o ejemplos sin credenciales reales.

## 5. Modelo de datos existente

### `categorias`

- `id`: BIGINT, clave primaria, autoincremental.
- `nombre`: VARCHAR(100), obligatorio y único.

### `productos`

- `id`: BIGINT, clave primaria, autoincremental.
- `codigo`: VARCHAR(50), obligatorio y único.
- `nombre`: VARCHAR(150), obligatorio.
- `categoria_id`: BIGINT, clave foránea a `categorias.id`.
- `precio`: DECIMAL(12,2), obligatorio y mayor que cero.
- `existencia`: INT, obligatoria y mayor o igual que cero.

Las futuras entidades Java deben respetar estos nombres y tipos. El precio se representará con `BigDecimal` y los identificadores con `Long`.

## 6. Estado de Git

Commits cuya creación y subida quedaron comprobadas en la conversación:

| Commit | Descripción | Confirmación |
|---|---|---|
| `ee63b8b` | Preparar documentación inicial y archivos ignorados. | Incluido en el primer push mostrado. |
| `47e24fe` | Excluir configuración local de IntelliJ. | Push confirmado. |
| `faf7547` | Crear estructura inicial del microservicio de catálogo. | Push confirmado. |
| `fec1904` | Configurar persistencia del catálogo con Mysql. | Commit comprobado el 30 de octubre; coincide con la referencia local `origin/main`. |

Avance actual pendiente de commit:

- Cinco archivos Java nuevos para la consulta de categorías.
- `AVANCES.md` y `database/usuario_catalogo.example.sql` aparecían sin seguimiento al retomar.
- Probar primero el endpoint; después revisar los archivos y realizar un commit con el avance real.
- Plantilla del usuario revisada el 30 de octubre: utiliza CAMBIAR_AL_CONFIGURAR. Conservar el marcador al añadirla a Git.

Desde la carpeta principal, comprobar:

```powershell
git status
```

```powershell
git log --oneline -5
```

La conexión y el script de esquema ya están en `fec1904`. Registrar aquí el hash de la consulta de categorías cuando se confirme y se suba. No crear de nuevo el commit de conexión.

El documento pide al menos cinco commits reales y progresivos por microservicio y componente principal. Los commits iniciales del repositorio son preparación general; el progreso de cada componente debe poder identificarse en sus archivos e historial.

## 7. Próxima tarea: primera lectura real de categorías

**Objetivo:** `GET http://localhost:8081/api/categorias` debe consultar MySQL y devolver las categorías existentes.

Implementación preparada y compilada el 30 de octubre:

- [x] `Categoria`: entidad de la tabla existente.
- [x] `CategoriaRepository`: acceso a datos con Spring Data JPA.
- [x] `CategoriaResponse`: respuesta con identificador y nombre.
- [x] `CategoriaService`: consulta ordenada por nombre y conversión a DTO.
- [x] `CategoriaController`: exposición de la ruta HTTP.
- [x] Compilación con Maven y Java 21.

Orden de trabajo y comprobación:

1. Revisar el estado de Git y confirmar el avance anterior.
2. Asegurar que IntelliJ reconoce `catalogo-service/pom.xml` como proyecto Maven y utiliza JDK 21.
3. Crear los paquetes de catálogo debajo de `com.prograiv.catalogo`.
4. Crear la entidad `Categoria`, mapeada a la tabla `categorias`.
5. Crear `CategoriaRepository` para leer los registros.
6. Crear `CategoriaService` para la operación de consulta.
7. Definir una respuesta con `id` y `nombre`.
8. Crear `CategoriaController` con la ruta `GET /api/categorias`.
9. Arrancar con la variable de contraseña configurada en esa terminal.
10. Abrir la ruta en el navegador y comprobar la respuesta.
11. Explicar brevemente controlador → servicio → repositorio → MySQL.
12. Registrar el avance real con commit y push; actualizar esta bitácora.

Estructura propuesta para este avance:

```text
com.prograiv.catalogo
  CatalogoServiceApplication
  entity/
    Categoria
  repository/
    CategoriaRepository
  service/
    CategoriaService
  dto/
    CategoriaResponse
  controller/
    CategoriaController
```

Condición para marcarlo terminado:

- [ ] La aplicación inicia con `ddl-auto=validate`.
- [x] La ruta responde HTTP 200.
- [x] Los datos proceden de MySQL.
- [x] La respuesta incluye Perifericos y Monitores.
- [x] Los datos conservan sus identificadores y nombres.
- [ ] El avance está confirmado y subido a GitHub.

## 8. Cómo arrancar al volver

Comprobar que el servicio de MySQL esté ejecutándose. Workbench permite probar la conexión; la ventana de Workbench no necesita permanecer abierta para que MySQL funcione.

En la terminal PowerShell de IntelliJ, ejecutar cada bloque completo por separado:

```powershell
cd "C:\Users\davis\IdeaProjects\Proyecto-Programacion-Final\catalogo-service"
```

```powershell
$catalogoClave = Read-Host "Contrasena de catalogo_app" -AsSecureString
```

En este punto, escribir la contraseña de `catalogo_app` y pulsar Enter. Los caracteres quedan ocultos. Después:

```powershell
$env:CATALOGO_DB_PASSWORD = [System.Net.NetworkCredential]::new("", $catalogoClave).Password
```

```powershell
.\mvnw.cmd spring-boot:run
```

La variable debe estar disponible en la misma terminal desde la que se ejecuta Maven. Este procedimiento configura la contraseña para esa sesión.

Para detener la aplicación y recuperar el indicador de PowerShell, pulsar `Ctrl + C`.

Para trabajar con Git desde la raíz, después de detenerla:

```powershell
cd "C:\Users\davis\IdeaProjects\Proyecto-Programacion-Final"
```

## 9. Continuaciones pendientes, en orden

### A. Completar catálogo

- [x] Consulta de categorias.
- [ ] Entidad y persistencia de productos.
- [ ] DTO de entrada y salida.
- [ ] Registro por lote con respuesta individual y correlación por elemento.
- [ ] Validar campos obligatorios, categoría, código único, precio y existencia.
- [ ] Mantener productos válidos aunque otros elementos del lote fallen.
- [ ] Detectar códigos repetidos dentro del lote y en la base.
- [ ] Listado de productos con código, nombre, categoría, precio y existencia.
- [ ] Detalle individual.
- [ ] Búsqueda, filtros y paginación.
- [ ] Respuestas de error comprensibles.
- [ ] Swagger/OpenAPI y eventos con Logback.

### B. Crear Eureka y Gateway

- [ ] Practicar registro y enrutamiento con una consulta pequeña de catálogo.
- [ ] Crear Eureka Server independiente.
- [ ] Registrar catálogo e importaciones.
- [ ] Crear Gateway y sus rutas.
- [ ] Probar consultas por Gateway.
- [ ] Hacer que importaciones encuentre catálogo por nombre mediante Eureka.
- [ ] Conservar evidencias de que descubrimiento y enrutamiento participan realmente.

### C. Completar importaciones

- [ ] Crear el microservicio y su base lógica independiente.
- [ ] Recibir CSV por multipart.
- [ ] Validar archivo vacío, UTF-8, encabezados, columnas y tipos.
- [ ] Conservar referencias de filas originales.
- [ ] Enviar productos al catálogo mediante HTTP y descubrimiento.
- [ ] Relacionar cada resultado con la fila correspondiente.
- [ ] Guardar historial, resumen y errores.
- [ ] Consultar historial, detalle y errores por API.
- [ ] Resolver estados y fallos técnicos sin afirmar éxitos no confirmados.
- [ ] Documentar API y agregar logs útiles.

### D. Completar frontend PHP MVC

- [ ] Preparar PHP y la separación MVC.
- [ ] Crear el cliente HTTP hacia Gateway.
- [ ] Inicio con indicadores y resultados recientes.
- [ ] Carga de archivos.
- [ ] Resultado con filas aceptadas, rechazadas y motivos.
- [ ] Consulta de productos con filtros y paginación.
- [ ] Detalle de producto.
- [ ] Historial y consulta de importaciones anteriores.
- [ ] Manejo de mensajes de error.

### E. Cierre técnico y entrega

- [ ] CSV válidos y mixtos, con errores de estructura y de negocio.
- [ ] Pruebas de duplicados, campos vacíos, categorías, precios y existencias.
- [ ] Pruebas de integración, persistencia y fallos de comunicación.
- [ ] Scripts y configuración reproducibles.
- [ ] Documentación de API y evidencias de ejecución.
- [ ] README con requisitos, versiones, configuración y orden de arranque.
- [ ] Historial suficiente de avances reales por componente.
- [ ] Ejecución completa siguiendo solo las instrucciones de entrega.

### F. Comprensión y defensa

- [ ] Explicar el recorrido completo de una importación.
- [ ] Explicar clases, métodos, anotaciones y decisiones del código propio.
- [ ] Distinguir las responsabilidades de PHP, Gateway, Eureka y los dos servicios.
- [ ] Justificar validaciones, relaciones SQL y éxito parcial.
- [ ] Seguir y corregir un error desde la interfaz hasta la base.
- [ ] Practicar cambios pequeños con tiempo limitado.
- [ ] Hacer un simulacro de entrevista con preguntas una por una.

## 10. Plan para la siguiente sesión

El viernes 30 de octubre el estudiante pidió retomar y avanzar lo máximo posible durante el día. La preferencia sigue siendo completar la implementación y después profundizar en comprensión y defensa. La planificación se ajustará según los resultados comprobados.

Prioridades:

1. Terminar la lectura de categorías y el catálogo funcional.
2. Integrar Eureka y Gateway temprano para descubrir dificultades de comunicación.
3. Completar el flujo CSV → importaciones → catálogo → historial y errores.
4. Conectar las pantallas PHP MVC.
5. Cerrar pruebas, documentación y evidencias.
6. Reservar tiempo para comprender y practicar la defensa.

Cada bloque se considera completo cuando funciona, se prueba, se registra en Git y se actualiza aquí. Mantener visibles los requisitos pendientes permite ajustar el plan sin perderlos.

## 11. Dificultades resueltas y recordatorios

| Situación | Solución o recordatorio |
|---|---|
| Git pidió identidad de autor. | Se configuraron nombre y correo para el repositorio. |
| `.idea` entró en el primer commit. | Se retiró del seguimiento y se agregó al `.gitignore`. |
| PowerShell no reconocía `java`. | Se localizó el JDK 21 y se configuraron `JAVA_HOME` y Path. |
| Había un nivel de lenguaje 27/Preview. | Se indicó elegir Java 21 normal o `SDK default`; verificar al retomar. |
| No aparecía Spring Boot 3.5 en Initializr. | Se eligió Spring Boot 4.0.9; mantener versiones compatibles para Spring Cloud y OpenAPI. |
| El paquete tenía un guion. | Se corrigió a `com.prograiv.catalogo`. |
| La extracción creó dos carpetas `catalogo-service`. | Se movió el contenido al nivel correcto. |
| `git add` buscó una carpeta inexistente. | Se regresó a la raíz del repositorio antes de agregar la carpeta del servicio. |
| Avisos LF → CRLF. | Los commits y pushes mostrados se completaron; son avisos de conversión de saltos de línea. |
| Se ejecutó una ruta como comando. | Una ubicación informativa se abre en el Explorador o se usa como argumento del comando apropiado. |
| Confusión al guardar SQL. | La carpeta se elige arriba; abajo se escribe el nombre `01_catalogo.sql`. |
| Dependencias aún no descargadas tras guardar el POM. | Se ejecutó `mvnw.cmd dependency:resolve`, con `BUILD SUCCESS`. |

## 12. Protocolo para mantener ordenado el trabajo

Al comenzar una sesión:

1. Leer el resumen y la próxima tarea de este archivo.
2. Revisar `git status` y los últimos commits.
3. Comprobar qué aplicaciones están ejecutándose.
4. Elegir un objetivo concreto con una condición de cierre.

Al terminar un avance:

1. Registrar qué se implementó y qué se comprobó.
2. Anotar errores o limitaciones pendientes.
3. Confirmar los archivos apropiados con Git y hacer push.
4. Registrar el hash cuando esté disponible.
5. Actualizar el estado y la siguiente acción de esta bitácora.

Al terminar una sesión:

1. Dejar una próxima tarea concreta, con archivo o componente y resultado esperado.
2. Documentar lo que falta confirmar sin marcarlo como terminado.
3. Guardar este archivo dentro del repositorio como `AVANCES.md`.
4. Confirmar y subir también la actualización de la bitácora.

Plantilla para nuevas entradas:

```markdown
### Fecha — Objetivo del avance

- Realizado:
- Comprobado mediante:
- Archivos modificados:
- Commit:
- Push confirmado:
- Pendientes o errores:
- Próxima acción:
```

## 13. Registro de la sesión del 30 de octubre

### Primera consulta de categorías

- **Revisión inicial:** solo existía la clase principal Java. Se revisaron POM, configuración, bitácora y estado de Git.
- **Git confirmado:** `fec1904` ya contiene la conexión y coincide con `origin/main` en el repositorio local. La bitácora y la plantilla de usuario estaban sin seguimiento.
- **Implementado:** cinco archivos en los paquetes `entity`, `repository`, `dto`, `service` y `controller`.
- **Ruta:** `GET /api/categorias`.
- **Comportamiento diseñado:** leer la tabla `categorias`, ordenar por nombre y devolver una lista JSON con `id` y `nombre`. Una tabla vacía produce una lista vacía.
- **Comprobado:** `mvnw.cmd -DskipTests compile` finalizó con `BUILD SUCCESS`, compilando seis archivos fuente con Java 21.
- **Pendiente:** arrancar con la contraseña local, comprobar HTTP 200 y ver las categorías reales. No se ejecutaron pruebas automáticas ni se afirmó una validación funcional con la base en esta sesión.
- **Plantilla de usuario:** se sustituyó el valor de contraseña del archivo de ejemplo por CAMBIAR_AL_CONFIGURAR; la contraseña del servidor permanece igual.
- **Commit:** pendiente de completar la comprobación funcional y revisar los cambios.
- **Próxima continuación:** entidades y operaciones de productos, manteniendo la separación entre capas.

Referencias de apoyo: [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/) y [API REST con Spring](https://spring.io/guides/gs/rest-service/).

### Incidencia de autenticacion al probar categorias

- El arranque del 30 de octubre fallo con MySQL 1045: Access denied para catalogo_app@localhost, con contrasena enviada.
- El mensaje posterior sobre el dialecto de Hibernate aparece tras el rechazo de conexion; primero se comprobara la autenticacion.
- Siguiente comprobacion: ejecutar el cliente MySQL como catalogo_app y consultar categorias con la contrasena local del usuario.
- La plantilla SQL contiene un marcador de documentacion; editarla no cambia la contrasena del usuario existente en MySQL.
- Estado historico: el arranque fallo por autenticacion. La prueba SQL posterior ya fue correcta; falta arrancar de nuevo con la credencial actualizada.
### Autenticacion del usuario de catalogo comprobada

- El estudiante informo que establecio una nueva contrasena para catalogo_app desde Workbench con ALTER USER.
- La prueba directa con mysql.exe termino correctamente y consulto catalogo_db.categorias.
- Resultado observado: id 2 Monitores e id 1 Perifericos.
- La autenticacion y la lectura SQL con el usuario de aplicacion quedan comprobadas. La contrasena no se registra en esta bitacora.
- Siguiente paso: volver a proporcionar CATALOGO_DB_PASSWORD en la terminal, arrancar Spring Boot y probar GET /api/categorias.
- La prueba HTTP y el commit del endpoint siguen pendientes.
### Consulta HTTP de categorias completada

- El estudiante compartio la respuesta JSON con Monitores (2) y Perifericos (1).
- Se verifico tambien HTTP 200 desde la API local, con los mismos datos.
- Queda comprobado el recorrido controlador, servicio, repositorio y MySQL.
- El error de autenticacion anterior esta resuelto para la ejecucion actual.
- Proximo bloque: entidad Producto, listado paginado, filtros por codigo/nombre/categoria y detalle individual.