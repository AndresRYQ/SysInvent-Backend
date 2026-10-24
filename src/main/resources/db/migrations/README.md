# Migraciones manuales de SysInvent

Este directorio contiene los scripts SQL versionados utilizados para crear y actualizar la base de datos de SysInvent.

## Motor de base de datos

- SQL Server
- Base de datos: `sysinvent_db`

## Orden de ejecución

Los scripts deben ejecutarse respetando el número de versión:

| Versión | Archivo | Descripción |
|---|---|---|
| V001 | `V001__seguridad_usuarios_roles.sql` | Crea roles, usuarios y asignaciones de roles. |
| V002 | `V002__catalogos_productos.sql` | Crea catálogos, proveedores, contactos y productos. |
| V003 | `V003__operaciones_almacen.sql` | Crea ingresos, vales, distribuciones, kardex y bitácora. |
| V004 | `V004__modulos_datos_iniciales.sql` | Crea módulos, permisos y datos iniciales. |

## Procedimiento de ejecución

1. Realizar una copia de seguridad de la base de datos.
2. Abrir SQL Server Management Studio.
3. Conectarse al servidor SQL Server.
4. Ejecutar primero `V001`.
5. Continuar con `V002`, `V003` y `V004`.
6. Si un script presenta un error, detener la ejecución y no continuar con la siguiente versión.
7. Registrar la fecha, el responsable y el resultado de cada ejecución.

## Reglas para nuevas migraciones

- No modificar un script que ya fue ejecutado en un ambiente.
- Cada cambio nuevo debe crearse en un archivo con una versión superior.
- El siguiente cambio deberá llamarse `V005__descripcion_del_cambio.sql`.
- Los scripts deben incluir validaciones para evitar registros u objetos duplicados.
- No se deben incluir contraseñas en texto plano.
- Antes de ejecutar una migración en producción debe probarse en desarrollo.
- Siempre se debe crear una copia de seguridad antes de aplicar cambios estructurales.
- Las migraciones deben guardarse y versionarse mediante Git.

## Características de seguridad

Los scripts incluyen:

- Claves primarias.
- Claves foráneas.
- Restricciones únicas.
- Restricciones de validación `CHECK`.
- Valores predeterminados.
- Protección de movimientos del kardex mediante un trigger.
- Inserciones idempotentes mediante `NOT EXISTS`.
- Creación idempotente de tablas mediante `OBJECT_ID`.

## Registro de ejecución

| Versión | Ambiente | Fecha | Responsable | Resultado |
|---|---|---|---|---|
| V001 | Desarrollo | Pendiente | Equipo SysInvent | Pendiente |
| V002 | Desarrollo | Pendiente | Equipo SysInvent | Pendiente |
| V003 | Desarrollo | Pendiente | Equipo SysInvent | Pendiente |
| V004 | Desarrollo | Pendiente | Equipo SysInvent | Pendiente |

## Esquema consolidado

El archivo `../schema.sql` se conserva como una vista completa y consolidada del esquema.

Los archivos de esta carpeta representan el historial versionado y ordenado de creación de la base de datos.