/*
 * V002 - Catalogos maestros, proveedores, contactos y productos.
 * Requiere haber ejecutado previamente V001.
 */

USE [sysinvent_db];
GO

/* ============================================================
   TABLAS MAESTRAS DE INVENTARIO
   ============================================================ */

IF OBJECT_ID(N'dbo.tipo_producto', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.tipo_producto (
        tipo_producto_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_tipo_producto_activo DEFAULT (1),
        nombre VARCHAR(100) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_tipo_producto PRIMARY KEY (tipo_producto_id),
        CONSTRAINT uq_tipo_producto_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.categoria', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.categoria (
        categoria_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_categoria_activo DEFAULT (1),
        nombre VARCHAR(100) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_categoria PRIMARY KEY (categoria_id),
        CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.unidad_medida', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.unidad_medida (
        unidad_medida_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_unidad_medida_activo DEFAULT (1),
        nombre VARCHAR(100) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_unidad_medida PRIMARY KEY (unidad_medida_id),
        CONSTRAINT uq_unidad_medida_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.proveedor', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.proveedor (
        proveedor_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_proveedor_activo DEFAULT (1),
        ruc VARCHAR(20) NOT NULL,
        razon_social VARCHAR(255) NOT NULL,
        correo VARCHAR(150) NULL,
        telefono VARCHAR(30) NULL,
        direccion VARCHAR(255) NULL,
        CONSTRAINT pk_proveedor PRIMARY KEY (proveedor_id),
        CONSTRAINT uq_proveedor_ruc UNIQUE (ruc)
    );
END;
GO

IF OBJECT_ID(N'dbo.contacto', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.contacto (
        contacto_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_contacto_activo DEFAULT (1),
        proveedor_id INT NOT NULL,
        nombre_completo VARCHAR(255) NOT NULL,
        cargo VARCHAR(150) NULL,
        telefono VARCHAR(30) NULL,
        correo VARCHAR(150) NULL,
        CONSTRAINT pk_contacto PRIMARY KEY (contacto_id),
        CONSTRAINT fk_contacto_proveedor FOREIGN KEY (proveedor_id)
            REFERENCES dbo.proveedor (proveedor_id)
    );
END;
GO

IF OBJECT_ID(N'dbo.tipo_documento', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.tipo_documento (
        tipo_documento_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_tipo_documento_activo DEFAULT (1),
        nombre VARCHAR(100) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_tipo_documento PRIMARY KEY (tipo_documento_id),
        CONSTRAINT uq_tipo_documento_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.centro_costo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.centro_costo (
        centro_costo_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_centro_costo_activo DEFAULT (1),
        nombre VARCHAR(150) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_centro_costo PRIMARY KEY (centro_costo_id),
        CONSTRAINT uq_centro_costo_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.destino', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.destino (
        destino_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_destino_activo DEFAULT (1),
        nombre VARCHAR(150) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_destino PRIMARY KEY (destino_id),
        CONSTRAINT uq_destino_nombre UNIQUE (nombre)
    );
END;
GO

IF OBJECT_ID(N'dbo.parte_equipo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.parte_equipo (
        parte_equipo_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_parte_equipo_activo DEFAULT (1),
        codigo VARCHAR(50) NOT NULL,
        nombre VARCHAR(150) NOT NULL,
        descripcion VARCHAR(255) NULL,
        CONSTRAINT pk_parte_equipo PRIMARY KEY (parte_equipo_id),
        CONSTRAINT uq_parte_equipo_codigo UNIQUE (codigo)
    );
END;
GO

IF OBJECT_ID(N'dbo.producto', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.producto (
        producto_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        activo BIT NOT NULL CONSTRAINT df_producto_activo DEFAULT (1),
        codigo VARCHAR(50) NOT NULL,
        nombre VARCHAR(255) NOT NULL,
        descripcion VARCHAR(255) NULL,
        tipo_producto_id INT NOT NULL,
        categoria_id INT NOT NULL,
        unidad_medida_id INT NOT NULL,
        proveedor_id INT NOT NULL,
        stock_actual DECIMAL(18,4) NOT NULL CONSTRAINT df_producto_stock_actual DEFAULT (0),
        stock_minimo DECIMAL(18,4) NOT NULL CONSTRAINT df_producto_stock_minimo DEFAULT (0),
        precio_unitario DECIMAL(18,4) NOT NULL CONSTRAINT df_producto_precio_unitario DEFAULT (0),
        CONSTRAINT pk_producto PRIMARY KEY (producto_id),
        CONSTRAINT uq_producto_codigo UNIQUE (codigo),
        CONSTRAINT fk_producto_tipo_producto FOREIGN KEY (tipo_producto_id)
            REFERENCES dbo.tipo_producto (tipo_producto_id),
        CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id)
            REFERENCES dbo.categoria (categoria_id),
        CONSTRAINT fk_producto_unidad_medida FOREIGN KEY (unidad_medida_id)
            REFERENCES dbo.unidad_medida (unidad_medida_id),
        CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id)
            REFERENCES dbo.proveedor (proveedor_id),
        CONSTRAINT ck_producto_stock_actual CHECK (stock_actual >= 0),
        CONSTRAINT ck_producto_stock_minimo CHECK (stock_minimo >= 0),
        CONSTRAINT ck_producto_precio_unitario CHECK (precio_unitario >= 0)
    );
END;
GO