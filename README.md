# SysInvent-Backend

API backend para SysInvent basada en Java 17, Spring Boot y Spring Data JPA. Soporta SQL Server (perfil `local`/`prod`) y MySQL 8 (perfil `mysql`).

## Configuración local

1. Copia `.env.example` a `.env`.
2. Define en `.env` la URL JDBC y las credenciales de SQL Server.
3. Genera un `JWT_SECRET` aleatorio de al menos 32 bytes. Para crear el administrador inicial, define `INITIAL_ADMIN_PASSWORD` con una contraseña segura.
4. Ejecuta el esquema de `src/main/resources/db/schema.sql` en la base de datos SQL Server configurada.
5. Ejecuta la aplicación con `mvnw.cmd spring-boot:run`.

La aplicación importa `.env` localmente. No subas `.env` al control de versiones. En producción, inyecta las mismas variables desde el gestor de secretos del entorno.

## Configuración local con MySQL 8

1. Crea el esquema y los datos iniciales:

   ```powershell
   mysql -u root -p < src/main/resources/db/mysql/schema_mysql.sql
   ```

2. En `.env` define:

   ```properties
   SPRING_PROFILES_ACTIVE=mysql
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/sysinvent_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=<tu contraseña>
   ```

3. Ejecuta `mvnw.cmd spring-boot:run`.

Las consultas nativas del backend usan SQL estándar (sin `GETDATE`, `TOP`, `CONVERT`, `STRING_AGG`, `DATEADD`), por lo que el mismo código funciona en ambos motores.

## Dashboard y reportes (SCRUM-55)

| Endpoint | Descripción |
|---|---|
| `GET /api/dashboard` | Indicadores (productos activos, stock crítico/bajo/sin stock, ingresos y vales del mes, movimientos de hoy), alertas y últimos 5 movimientos. |
| `GET /api/reportes/ingresos` | Reporte paginado de ingresos con filtros de búsqueda, proveedor, tipo, producto, estado y fechas. |
| `GET /api/reportes/vales` | Reporte paginado de vales con filtros de centro de costo, destino, parte de equipo, tipo, producto, estado y fechas. |
| `GET /api/reportes/productos-mas-pedidos` | Ranking de productos solicitados en vales registrados. |
| `GET /api/reportes/kardex` | Movimientos, resumen y stock actual; filtros `productoId`, `desde`, `hasta`, `tipoMovimiento` (`ENTRADA`, `SALIDA` o tipo exacto). |
| `GET /api/reportes/filtros` | Catálogos activos para poblar los filtros de los reportes. |

## Validación

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
```