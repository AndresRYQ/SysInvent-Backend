/*
 * V001 - Seguridad base: roles, usuarios y asignacion de roles.
 * Motor: SQL Server.
 */

IF DB_ID(N'sysinvent_db') IS NULL
BEGIN
    CREATE DATABASE [sysinvent_db];
END;
GO

USE [sysinvent_db];
GO

IF OBJECT_ID(N'dbo.rol', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.rol (
        rol_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NULL,
        nombre VARCHAR(255) NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_rol PRIMARY KEY (rol_id)
    );
END;
GO

IF OBJECT_ID(N'dbo.usuario', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuario (
        usuario_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NULL,
        usuario VARCHAR(255) NULL,
        nombres VARCHAR(255) NULL,
        ape_paterno VARCHAR(255) NULL,
        ape_materno VARCHAR(255) NULL,
        dni VARCHAR(255) NULL,
        codigo VARCHAR(255) NULL,
        email VARCHAR(255) NULL,
        contrasena VARCHAR(255) NULL,
        CONSTRAINT pk_usuario PRIMARY KEY (usuario_id)
    );
END;
GO

IF OBJECT_ID(N'dbo.usuario_rol', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuario_rol (
        usuario_rol_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        usuario_id INT NULL,
        rol_id INT NULL,
        activo BIT NULL,
        fecha_asignacion DATE NULL,
        CONSTRAINT pk_usuario_rol PRIMARY KEY (usuario_rol_id),
        CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (usuario_id)
            REFERENCES dbo.usuario (usuario_id),
        CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (rol_id)
            REFERENCES dbo.rol (rol_id)
    );
END;
GO