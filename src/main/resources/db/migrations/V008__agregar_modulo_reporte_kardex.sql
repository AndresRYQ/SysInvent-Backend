/*
 * V008 - Agrega el módulo independiente de Reporte Kardex.
 * Permite asignar el acceso desde la administración de roles.
 */

USE [sysinvent_db];
GO

INSERT INTO dbo.modulo (
    fecha_registro,
    activo,
    nombre,
    descripcion,
    ruta
)
SELECT
    CAST(SYSDATETIME() AS DATE),
    1,
    'Reporte Kardex',
    'Consulta de movimientos y trazabilidad del inventario',
    '/reportes/kardex'
WHERE NOT EXISTS (
    SELECT 1
    FROM dbo.modulo
    WHERE ruta = '/reportes/kardex'
       OR nombre = 'Reporte Kardex'
);
GO

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
  AND modulo.ruta = '/reportes/kardex'
  AND NOT EXISTS (
      SELECT 1
      FROM dbo.rol_modulo permiso
      WHERE permiso.rol_id = rol.rol_id
        AND permiso.modulo_id = modulo.modulo_id
  );
GO