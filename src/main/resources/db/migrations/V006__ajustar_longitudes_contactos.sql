/* V006 - Alinea el telefono de contactos con la validacion de 9 digitos. */
USE [sysinvent_db];
GO

ALTER TABLE dbo.contacto ALTER COLUMN telefono VARCHAR(9) NULL;
GO
