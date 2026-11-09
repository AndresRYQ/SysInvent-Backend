# SysInvent-Backend

API backend para SysInvent basada en Java 17, Spring Boot, Spring Data JPA y SQL Server.

## Configuración local

1. Copia `.env.example` a `.env`.
2. Define en `.env` la URL JDBC y las credenciales de SQL Server.
3. Genera un `JWT_SECRET` aleatorio de al menos 32 bytes. Para crear el administrador inicial, define `INITIAL_ADMIN_PASSWORD` con una contraseña segura.
4. Ejecuta el esquema de `src/main/resources/db/schema.sql` en la base de datos SQL Server configurada.
5. Ejecuta la aplicación con `mvnw.cmd spring-boot:run`.

La aplicación importa `.env` localmente. No subas `.env` al control de versiones. En producción, inyecta las mismas variables desde el gestor de secretos del entorno.

## Perfiles de ejecución

- `local`: perfil predeterminado para desarrollo. Importa las variables desde `.env`.
- `prod`: perfil de producción. Recibe las variables directamente del entorno.
- `test`: perfil reservado para la ejecución automatizada de pruebas.

El contenedor Docker utiliza `prod` de manera predeterminada. Este valor puede reemplazarse mediante la variable `SPRING_PROFILES_ACTIVE`.

## Ejecución con Docker

Construir la imagen:

```powershell
docker build -t sysinvent-backend .
```

Ejecutar el contenedor usando las variables locales:

```powershell
docker run --rm --env-file .env -p 8080:8080 sysinvent-backend
```

La aplicación queda disponible en `http://localhost:8080`.


## Validación

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
```