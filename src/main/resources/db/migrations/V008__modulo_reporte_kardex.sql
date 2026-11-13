/*
 * V008 - Registra el módulo "Reporte de Kardex" y lo asigna al rol ADMINISTRADOR.
 * Requiere haber ejecutado V001..V007.
 */

USE [sysinvent_db];
GO

INSERT INTO dbo.modulo (fecha_registro, activo, nombre, ruta)
SELECT CAST(SYSDATETIME() AS DATE), 1, 'Reporte de Kardex', '/reportes/kardex'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.modulo WHERE nombre = 'Reporte de Kardex'
);
GO

INSERT INTO dbo.rol_modulo (fecha_registro, rol_id, modulo_id, activo)
SELECT CAST(SYSDATETIME() AS DATE), rol.rol_id, modulo.modulo_id, 1
FROM dbo.rol rol
CROSS JOIN dbo.modulo modulo
WHERE rol.nombre = 'ADMINISTRADOR'
  AND modulo.nombre = 'Reporte de Kardex'
  AND NOT EXISTS (
      SELECT 1 FROM dbo.rol_modulo permiso
      WHERE permiso.rol_id = rol.rol_id
        AND permiso.modulo_id = modulo.modulo_id
  );
GO
