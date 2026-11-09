
/* V005 - Alinea las longitudes del proveedor con las reglas de validación. */

USE [sysinvent_db];
GO

ALTER TABLE dbo.proveedor ALTER COLUMN ruc VARCHAR(11) NOT NULL;
ALTER TABLE dbo.proveedor ALTER COLUMN telefono VARCHAR(9) NULL;
GO
