# SysInvent Backend

API REST para administrar catálogos y operaciones de inventario: ingresos de almacén, vales de consumo, existencias y movimientos de Kardex. El backend también incluye autenticación, administración de usuarios y roles, bitácora, dashboard y reportes.

## Contenido

- [Inicio rápido](#inicio-rápido)
- [Tecnologías](#tecnologías)
- [Arquitectura y estructura](#arquitectura-y-estructura)
- [Módulos y flujo de inventario](#módulos-y-flujo-de-inventario)
- [Base de datos](#base-de-datos)
- [API REST](#api-rest)
- [Seguridad y configuración](#seguridad-y-configuración)
- [DTOs](#dtos)
- [Pruebas](#pruebas)

## Inicio rápido

### Requisitos de configuración

La aplicación utiliza SQL Server. Antes de iniciarla:

1. Configura las variables de conexión indicadas en [Seguridad y configuración](#seguridad-y-configuración).
2. Ejecuta [`src/main/resources/db/schema.sql`](src/main/resources/db/schema.sql) en SQL Server si la base de datos y sus tablas aún no existen.
3. Opcionalmente, define `INITIAL_ADMIN_PASSWORD` para crear el usuario inicial `admin` cuando no exista un usuario activo con ese nombre.

### Ejecutar la aplicación

```powershell
.\mvnw.cmd spring-boot:run
```

El puerto predeterminado es `8080`; puede cambiarse con `PORT`.

## Tecnologías

| Tecnología | Versión / detalle |
|---|---|
| Java | 17 |
| Spring Boot | 3.0.6 |
| Spring Security | `spring-boot-starter-security`; versión administrada por Spring Boot |
| Spring Data JPA | `spring-boot-starter-data-jpa`; versión administrada por Spring Boot |
| JWT | JJWT 0.11.5 (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) |
| SQL Server | Driver `mssql-jdbc`; versión administrada por Spring Boot |
| Swagger/OpenAPI | Springdoc OpenAPI 2.0.4 |
| Maven | Maven Wrapper incluido; versión de Maven no fijada en `pom.xml` |
| Lombok | 1.18.30 |

El proyecto también usa Spring Web, Bean Validation y Spring Boot Test. H2 está incluido para las pruebas.

## Arquitectura y estructura

La aplicación sigue una arquitectura por capas:

```text
Controller → Service → Repository → Entity → SQL Server
```

Los controladores publican las rutas REST; los servicios coordinan la lógica de aplicación y las transacciones; los repositorios acceden a los datos mediante Spring Data JPA; y las entidades representan el modelo persistido. Los DTOs Request y Response separan los datos HTTP de las entidades.

El filtro JWT se integra con Spring Security. El inicio de sesión valida credenciales, consulta el rol y los módulos asignados y genera un token firmado con HS256.

```text
src/
├── main/
│   ├── java/com/agrihusac/SysInvent/
│   │   ├── api/             Controladores REST
│   │   ├── common/          Excepciones comunes de negocio
│   │   ├── config/          Seguridad, CORS, Swagger, auditoría e inicialización
│   │   ├── exception/       Manejo de excepciones
│   │   ├── model/
│   │   │   ├── entity/      Entidades JPA
│   │   │   ├── mapper/      Mapeo entre modelos
│   │   │   ├── projection/  Proyecciones para consultas
│   │   │   ├── request/     DTOs de entrada por dominio
│   │   │   └── response/    DTOs de salida por dominio
│   │   ├── repository/      Repositorios de persistencia
│   │   ├── security/        Filtro JWT y manejadores de acceso
│   │   ├── service/         Servicios e implementaciones por dominio
│   │   ├── utils/           Utilidades, respuestas y paginación
│   │   └── validation/      Componentes de validación
│   └── resources/
│       ├── application.properties
│       ├── db/schema.sql
│       └── messages/validation.properties
└── test/
    ├── java/com/agrihusac/SysInvent/   Pruebas de contexto e integración
    └── resources/application.properties Configuración de pruebas con H2
```

## Módulos y flujo de inventario

Los módulos presentes en el código son:

- **Catálogos:** productos, categorías, tipos de producto, unidades de medida, proveedores, contactos, tipos de documento, centros de costo, destinos y partes de equipo.
- **Inventario:** ingresos de almacén, vales de consumo, control de existencias y Kardex.
- **Acceso:** inicio de sesión, usuarios, roles y asignación de módulos a roles.
- **Consultas:** dashboard y reportes de ingresos, vales, Kardex y productos más pedidos.
- **Auditoría:** consulta y registro de eventos en bitácora.

Flujo principal:

1. Los catálogos proporcionan las referencias utilizadas por productos, ingresos y vales.
2. Al registrar un ingreso, el servicio guarda el documento y sus detalles, incrementa el stock y registra movimientos `ENTRADA_INGRESO` en Kardex dentro de una transacción.
3. Al registrar un vale, el servicio valida las referencias y la disponibilidad de stock, descuenta las cantidades y registra movimientos `SALIDA_VALE`.
4. Los vales pueden actualizarse o anularse. La lógica del servicio contempla los ajustes de stock y Kardex relacionados.
5. Kardex permite consultar movimientos por producto, fechas y tipo, además del stock actual y los productos en nivel mínimo o inferior.

El esquema también admite los tipos de movimiento `AJUSTE_SALIDA`, `AJUSTE_ENTRADA` y `ANULACION_ENTRADA`; su existencia como valores permitidos no implica una ruta REST específica para generarlos.

## Base de datos

La configuración usa SQL Server y `SQLServerDialect`. Hibernate tiene `ddl-auto=none` y Spring SQL Init está desactivado (`spring.sql.init.mode=never`), por lo que el esquema debe ejecutarse por separado.

El script [`schema.sql`](src/main/resources/db/schema.sql) crea `sysinvent_db` si no existe, crea las tablas que faltan y define datos iniciales para módulos, roles y usuarios. No recrea ni actualiza tablas existentes.

| Área | Tablas |
|---|---|
| Seguridad y acceso | `usuario`, `rol`, `usuario_rol`, `modulo`, `rol_modulo` |
| Catálogos | `tipo_producto`, `categoria`, `unidad_medida`, `proveedor`, `contacto`, `tipo_documento`, `centro_costo`, `destino`, `parte_equipo`, `producto` |
| Ingresos | `ingreso_almacen`, `detalle_ingreso_almacen` |
| Consumo | `vale_consumo`, `detalle_vale_consumo`, `distribucion_vale_consumo` |
| Inventario y auditoría | `kardex_movimiento`, `bitacora` |

Relaciones principales:

- Un producto referencia tipo de producto, categoría, unidad de medida y proveedor.
- Un contacto pertenece a un proveedor. Un ingreso referencia proveedor, contacto y tipo de documento; sus detalles relacionan el ingreso con productos.
- Un vale referencia un centro de costo. Sus detalles relacionan el vale con productos y sus distribuciones con destinos y, opcionalmente, partes de equipo.
- Un movimiento de Kardex referencia un producto y un documento de origen (ingreso o vale); también puede incluir centro de costo, destino y parte de equipo.
- `usuario_rol` relaciona usuarios con roles y `rol_modulo` relaciona roles con módulos.
- La bitácora puede referenciar al usuario y al rol asociados al evento.

El esquema define claves foráneas, restricciones de unicidad y validaciones de cantidades, saldos y estados. Además, el trigger `tr_kardex_movimiento_inmutable` rechaza actualizaciones y eliminaciones de movimientos de Kardex.

## API REST

Las rutas corresponden a los mappings de los controladores. Todas requieren autenticación excepto `POST /api/login`, Swagger/OpenAPI y las solicitudes CORS `OPTIONS`.

### Autenticación y administración

| Método | Ruta |
|---|---|
| `POST` | `/api/login` |
| `GET`, `POST`, `PUT`, `DELETE` | `/api/usuarios` |
| `GET`, `POST`, `PUT`, `DELETE` | `/api/roles` |
| `GET` | `/api/usuarios/perfil/_plantilla` |
| `GET`, `POST` | `/api/bitacora` |
| `GET` | `/api/dashboard` |

### Catálogos

Cada ruta base listada expone `GET` y `POST`, `PUT /{id}` y `PUT /{id}/estado`. Las categorías y los productos también exponen `DELETE /{id}`; los productos, además, `GET /{id}`.

- `/api/v1/catalogos/categorias`
- `/api/v1/catalogos/centros-costo`
- `/api/v1/catalogos/contactos`
- `/api/v1/catalogos/destinos`
- `/api/v1/catalogos/partes-equipo`
- `/api/v1/catalogos/productos`
- `/api/v1/catalogos/proveedores`
- `/api/v1/catalogos/tipos-documento`
- `/api/v1/catalogos/tipos-producto`
- `/api/v1/catalogos/unidades-medida`

Otras rutas de consulta:

- `GET /api/v1/comun/tipos-producto`
- `GET /api/v1/comun/categorias`
- `GET /api/v1/comun/unidades-medida`
- `GET /api/v1/comun/proveedores`
- `GET /api/v1/comun/contactos`
- `GET /api/v1/comun/roles`
- `GET /api/v1/comun/centros-costo`
- `GET /api/v1/comun/productos`
- `GET /api/v1/comun/destinos`
- `GET /api/v1/comun/partes-equipo`
- `GET /api/v1/comun/tipos-documento`

También existe el controlador de categorías en `/api/categorias`, con `GET`, `POST` y `DELETE` en la ruta base.

### Operaciones de inventario

| Método | Ruta |
|---|---|
| `GET` | `/api/almacen/existencias` |
| `GET` | `/api/almacen/existencias/{productoId}/detalle` |
| `GET` | `/api/ingresos-almacen` |
| `GET` | `/api/ingresos-almacen/{id}` |
| `POST` | `/api/ingresos-almacen` |
| `GET` | `/api/vales-consumo` |
| `GET` | `/api/vales-consumo/{id}` |
| `POST` | `/api/vales-consumo` |
| `PUT` | `/api/vales-consumo/{id}` |
| `PATCH` | `/api/vales-consumo/{id}/anular` |
| `GET` | `/api/kardex/movimientos` |
| `GET` | `/api/kardex/stock` |

Los listados de ingresos y vales aceptan filtros y paginación. Los movimientos de Kardex se pueden filtrar por producto, fechas y tipo; `soloMinimo` permite consultar productos en nivel mínimo o inferior.

### Reportes

- `GET /api/reportes/ingresos`
- `GET /api/reportes/vales`
- `GET /api/reportes/productos-mas-pedidos`
- `GET /api/reportes/kardex`

## Seguridad y configuración

Spring Security exige autenticación para las rutas, excepto `POST /api/login`, `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**` y las solicitudes `OPTIONS`. El token se envía en `Authorization: Bearer <token>`. Las contraseñas se comparan con BCrypt; CSRF, HTTP Basic y el formulario de inicio de sesión están deshabilitados.

El inicio de sesión requiere credenciales válidas y exactamente un rol activo. El JWT incluye el usuario como sujeto, su identificador, correo y roles; los módulos se incluyen cuando corresponde. La firma usa HS256 y la expiración predeterminada es de `3600000` milisegundos.

Swagger/OpenAPI está configurado con autenticación HTTP Bearer en formato JWT. La aplicación escucha en `0.0.0.0` y usa el puerto `8080` por defecto, sobrescribible con `PORT`. Se importa opcionalmente `.env` en formato de propiedades.

| Variable | Uso |
|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC de SQL Server |
| `SPRING_DATASOURCE_USERNAME` | Usuario de conexión |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de conexión |
| `JWT_SECRET` | Secreto para firmar y validar tokens |
| `JWT_EXPIRATION_MS` | Expiración del token en milisegundos; predeterminado `3600000` |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos; predeterminado `http://localhost:3000,http://localhost:5173` |
| `INITIAL_ADMIN_PASSWORD` | Si se define y no existe el usuario activo `admin`, permite crear ese usuario con contraseña BCrypt |
| `PORT` | Puerto HTTP; predeterminado `8080` |

La cuenta usada para ejecutar `schema.sql` debe poder crear `sysinvent_db` si no existe y crear los objetos definidos en el script.

## DTOs

Los DTOs están organizados por dominio en `model/request` y `model/response`. Ejemplos de contratos:

| Operación | Solicitud | Respuesta |
|---|---|---|
| Inicio de sesión | `LoginRequest`: usuario y contraseña obligatorios | `LoginResponse` |
| Productos | `ProductoRequest`: datos del producto, IDs de catálogo, stock mínimo y precio | `ProductoResponse` |
| Ingresos | `IngresoAlmacenRequest`: referencias del documento y lista no vacía de detalles | `IngresoAlmacenResponse`; listados con `IngresoAlmacenListadoResponse` |
| Vales | `ValeConsumoRequest`: fecha, centro de costo, solicitante, motivo y detalles | `ValeConsumoResponse`; listados con `ValeConsumoListadoResponse` |
| Kardex | Parámetros de consulta | `KardexMovimientoResponse` y `StockProductoResponse` |

Las solicitudes usan Jakarta Bean Validation para reglas como campos obligatorios, longitudes, IDs positivos, valores no negativos de stock mínimo y precio, y detalles no vacíos. Las respuestas incluyen DTOs y proyecciones; también se usan estructuras comunes como `MessageResponse` y `CustomPage`.

## Pruebas

Las pruebas usan Spring Boot Test, MockMvc y H2 en memoria, configurado en `src/test/resources/application.properties` con `ddl-auto=create-drop`. No usan la configuración SQL Server de producción.

| Prueba | Cobertura indicada por los casos |
|---|---|
| `SysInventApplicationTests` | Carga del contexto de Spring |
| `CatalogosIntegrationTest` | Operaciones y validaciones de catálogos, relaciones de proveedores y productos, y consulta de roles |
| `IngresoAlmacenIntegrationTest` | Registro, stock, validaciones, atomicidad, documento duplicado y consulta por ID |
| `ValeConsumoIntegrationTest` | Autenticación, creación, actualización y anulación de vales, Kardex y falta de stock |
| `KardexIntegrationTest` | Stock y Kardex tras ingreso, consulta de movimientos y validación del rango de fechas |

Ejecutar la suite:

```powershell
.\mvnw.cmd test
```
