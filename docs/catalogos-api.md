# API de catálogos

## Alcance

La API de mantenimiento cubre únicamente las tablas de catálogo que ya existen
en el esquema SQL Server del proyecto:

| Catálogo | Ruta base |
|---|---|
| Categorías | `/api/v1/catalogos/categorias` |
| Centros de costo | `/api/v1/catalogos/centros-costo` |
| Contactos | `/api/v1/catalogos/contactos` |
| Destinos | `/api/v1/catalogos/destinos` |
| Partes de equipo | `/api/v1/catalogos/partes-equipo` |
| Proveedores | `/api/v1/catalogos/proveedores` |
| Tipos de documento | `/api/v1/catalogos/tipos-documento` |
| Tipos de producto | `/api/v1/catalogos/tipos-producto` |
| Unidades de medida | `/api/v1/catalogos/unidades-medida` |

Estados y tipos de movimiento no forman parte de este alcance porque el esquema
actual no define tablas para esos catálogos.

## Operaciones

Cada ruta base expone:

| Método | Ruta | Comportamiento |
|---|---|---|
| `GET` | ruta base | Lista registros activos, con filtro opcional `nombre`, `pagina` (base 1) y `tamPagina` (1–100). |
| `GET` | `/{id}` | Obtiene un registro por identificador. |
| `POST` | ruta base | Crea un registro. |
| `PUT` | `/{id}` | Actualiza un registro existente y activo. |
| `DELETE` | `/{id}` | Desactiva el registro; no lo elimina físicamente. |
| `PUT` | `/{id}/estado` | Cambia el estado lógico usando el cuerpo `{"activo": true}` o `{"activo": false}`. |

Los DTOs de cada catálogo definen los campos aceptados y devueltos. Para
categorías se usan `nombre`, `descripcion`, `activo` y `fechaRegistro`; los
campos de creación/edición no incluyen el identificador ni el estado.

## Respuestas y validaciones

- `201 Created`: registro creado.
- `200 OK`: lectura, actualización o cambio de estado completado.
- `400 Bad Request`: cuerpo inválido o validación de campos fallida.
- `404 Not Found`: identificador o referencia requerida inexistente.
- `409 Conflict`: valor único duplicado o conflicto de integridad.

Los campos obligatorios, longitudes y valores únicos se validan usando los DTOs,
servicios y restricciones del esquema existente. Las listas devuelven registros
activos; la consulta por identificador y el endpoint de estado permiten
consultar/reactivar un registro desactivado.

## Persistencia y pendientes

La API reutiliza SQL Server, JPA y las tablas del esquema existente. No crea
tablas ni migraciones Flyway, y no cambia la base de datos configurada.

La configuración actual de Spring Security deja permitidas todas las rutas
`/api/**` sin autenticación. Por tanto, estos endpoints aún no aplican
autorización por rol o permiso y no deben exponerse en producción hasta definir
y configurar las reglas de acceso.
