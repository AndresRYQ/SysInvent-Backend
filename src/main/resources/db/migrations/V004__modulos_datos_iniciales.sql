/*
 * V004 - Modulos, permisos y datos iniciales.
 * Requiere haber ejecutado V001, V002 y V003.
 */

USE [sysinvent_db];
GO

/* ============================================================
   TABLAS DE SEGURIDAD Y CONTROL DE ACCESO
   ============================================================ */

IF OBJECT_ID(N'dbo.modulo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.modulo (
        modulo_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_modulo_activo DEFAULT (1),
        nombre VARCHAR(100) NOT NULL,
        descripcion VARCHAR(255) NULL,
        ruta VARCHAR(255) NULL,
        CONSTRAINT pk_modulo PRIMARY KEY (modulo_id),
        CONSTRAINT uq_modulo_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.rol_modulo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.rol_modulo (
        rol_modulo_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        rol_id INT NOT NULL,
        modulo_id INT NOT NULL,
        activo BIT NOT NULL CONSTRAINT df_rol_modulo_activo DEFAULT (1),
        CONSTRAINT pk_rol_modulo PRIMARY KEY (rol_modulo_id),
        CONSTRAINT uq_rol_modulo UNIQUE (rol_id, modulo_id),
        CONSTRAINT fk_rol_modulo_rol FOREIGN KEY (rol_id)
            REFERENCES dbo.rol (rol_id),
        CONSTRAINT fk_rol_modulo_modulo FOREIGN KEY (modulo_id)
            REFERENCES dbo.modulo (modulo_id)
    );
END;
GO

/* ============================================================
   MODULOS DISPONIBLES
   ============================================================ */

INSERT INTO dbo.modulo (
    fecha_registro,
    activo,
    nombre,
    ruta
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    1,
    catalogo.nombre,
    catalogo.ruta
FROM (
    VALUES
        ('Roles', '/roles'),
        ('Usuarios', '/usuarios'),
        ('Tipos de producto', '/tipos-producto'),
        ('Proveedores', '/proveedores'),
        ('Tipos de documento', '/tipos-documento'),
        ('Destinos', '/destinos'),
        ('Productos', '/productos'),
        ('Unidades de medida', '/unidades-medida'),
        ('Categorías', '/categorias'),
        ('Centros de costo', '/centros-costos'),
        ('Bitácora', '/bitacora'),
        ('Reporte de ingresos', '/reportes/ingresos'),
        ('Reporte de vales', '/reportes/vales'),
        ('Productos más pedidos', '/reportes/productos-mas-pedidos'),
        ('Contactos', '/contactos'),
        ('Partes de equipo', '/partes-equipo'),
        ('Control de almacén', '/control-almacen'),
        ('Vales de consumo', '/vales-consumo'),
        ('Ingresos de almacén', '/ingresos-almacen'),
        ('Perfil de usuario', '/perfil')
) AS catalogo(nombre, ruta)
WHERE NOT EXISTS (
    SELECT 1
    FROM dbo.modulo modulo_existente
    WHERE modulo_existente.nombre = catalogo.nombre
);
GO

/* ============================================================
   ROL ADMINISTRADOR
   ============================================================ */

INSERT INTO dbo.rol (
    fecha_registro,
    activo,
    nombre,
    descripcion
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    1,
    'ADMINISTRADOR',
    'Rol con acceso completo al sistema'
WHERE NOT EXISTS (
    SELECT 1
    FROM dbo.rol rol_existente
    WHERE rol_existente.nombre = 'ADMINISTRADOR'
);
GO

/* ============================================================
   USUARIO INICIAL
   ============================================================ */

INSERT INTO dbo.usuario (
    fecha_registro,
    activo,
    usuario,
    nombres,
    ape_paterno,
    ape_materno,
    dni,
    codigo,
    email,
    contrasena
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    1,
    '71477205',
    'Carlos Jesus',
    'Chinchay',
    'Pecho',
    '71477205',
    'USR001',
    'carlos@yopmail.com',
    '$2a$10$Efa0BQyjCWDzNwwTwzcs6.6A2sceUD4fDCZXqDuDYbAjmX4HXlNjK'
WHERE NOT EXISTS (
    SELECT 1
    FROM dbo.usuario usuario_existente
    WHERE usuario_existente.usuario = '71477205'
);
GO

/* ============================================================
   ASIGNACION DEL ROL AL USUARIO INICIAL
   ============================================================ */

INSERT INTO dbo.usuario_rol (
    fecha_registro,
    usuario_id,
    rol_id,
    activo,
    fecha_asignacion
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    usuario.usuario_id,
    rol.rol_id,
    1,
    CAST(SYSDATETIME() AS DATE)
FROM dbo.usuario usuario
CROSS JOIN dbo.rol rol
WHERE usuario.usuario = '71477205'
  AND rol.nombre = 'ADMINISTRADOR'
  AND NOT EXISTS (
      SELECT 1
      FROM dbo.usuario_rol asignacion
      WHERE asignacion.usuario_id = usuario.usuario_id
        AND asignacion.rol_id = rol.rol_id
  );
GO

/* ============================================================
   PERMISOS DEL ROL ADMINISTRADOR
   ============================================================ */

INSERT INTO dbo.rol_modulo (
    fecha_registro,
    rol_id,
    modulo_id,
    activo
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    rol.rol_id,
    modulo.modulo_id,
    1
FROM dbo.rol rol
CROSS JOIN dbo.modulo modulo
WHERE rol.nombre = 'ADMINISTRADOR'
  AND NOT EXISTS (
      SELECT 1
      FROM dbo.rol_modulo permiso
      WHERE permiso.rol_id = rol.rol_id
        AND permiso.modulo_id = modulo.modulo_id
  );
GO