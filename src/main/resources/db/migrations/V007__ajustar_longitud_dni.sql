/* V007 - Alinea el DNI de usuarios con la validacion de 8 digitos. */
USE [sysinvent_db];
GO

ALTER TABLE dbo.usuario ALTER COLUMN dni VARCHAR(8) NULL;
GO
