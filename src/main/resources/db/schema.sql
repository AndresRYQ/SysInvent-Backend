/*
 * Esquema inicial de SysInvent para SQL Server.
 *
 * Las sentencias son idempotentes: no eliminan tablas existentes y solo
 * crean la base de datos y las tablas cuando aun no existen.
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

/* ============================================================
   TABLAS TRANSACCIONALES Y DE AUDITORIA
   ============================================================ */

IF OBJECT_ID(N'dbo.ingreso_almacen', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.ingreso_almacen (
        ingreso_almacen_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        numero_ingreso VARCHAR(30) NOT NULL,
        fecha_ingreso DATE NOT NULL,
        proveedor_id INT NOT NULL,
        contacto_id INT NOT NULL,
        tipo_documento_id INT NOT NULL,
        numero_documento VARCHAR(50) NOT NULL,
        observacion VARCHAR(500) NULL,
        estado VARCHAR(20) NOT NULL CONSTRAINT df_ingreso_almacen_estado DEFAULT ('REGISTRADO'),
        CONSTRAINT pk_ingreso_almacen PRIMARY KEY (ingreso_almacen_id),
        CONSTRAINT uq_ingreso_almacen_numero UNIQUE (numero_ingreso),
        CONSTRAINT fk_ingreso_almacen_proveedor FOREIGN KEY (proveedor_id)
            REFERENCES dbo.proveedor (proveedor_id),
        CONSTRAINT fk_ingreso_almacen_contacto FOREIGN KEY (contacto_id)
            REFERENCES dbo.contacto (contacto_id),
        CONSTRAINT fk_ingreso_almacen_tipo_documento FOREIGN KEY (tipo_documento_id)
            REFERENCES dbo.tipo_documento (tipo_documento_id),
        CONSTRAINT ck_ingreso_almacen_estado CHECK (estado IN ('REGISTRADO', 'ANULADO'))
    );
END;
GO

IF OBJECT_ID(N'dbo.detalle_ingreso_almacen', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.detalle_ingreso_almacen (
        detalle_ingreso_almacen_id INT IDENTITY(1,1) NOT NULL,
        ingreso_almacen_id INT NOT NULL,
        producto_id INT NOT NULL,
        cantidad DECIMAL(18,4) NOT NULL,
        precio_unitario DECIMAL(18,4) NOT NULL,
        CONSTRAINT pk_detalle_ingreso_almacen PRIMARY KEY (detalle_ingreso_almacen_id),
        CONSTRAINT fk_detalle_ingreso_ingreso FOREIGN KEY (ingreso_almacen_id)
            REFERENCES dbo.ingreso_almacen (ingreso_almacen_id),
        CONSTRAINT fk_detalle_ingreso_producto FOREIGN KEY (producto_id)
            REFERENCES dbo.producto (producto_id),
        CONSTRAINT ck_detalle_ingreso_cantidad CHECK (cantidad > 0),
        CONSTRAINT ck_detalle_ingreso_precio CHECK (precio_unitario >= 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.vale_consumo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.vale_consumo (
        vale_consumo_id INT IDENTITY(1,1) NOT NULL,
        fecha_registro DATE NULL,
        fecha_modificacion DATE NULL,
        numero_vale VARCHAR(30) NOT NULL,
        fecha_vale DATE NOT NULL,
        centro_costo_id INT NOT NULL,
        solicitante VARCHAR(255) NOT NULL,
        motivo VARCHAR(500) NULL,
        estado VARCHAR(20) NOT NULL CONSTRAINT df_vale_consumo_estado DEFAULT ('REGISTRADO'),
        CONSTRAINT pk_vale_consumo PRIMARY KEY (vale_consumo_id),
        CONSTRAINT uq_vale_consumo_numero UNIQUE (numero_vale),
        CONSTRAINT fk_vale_consumo_centro_costo FOREIGN KEY (centro_costo_id)
            REFERENCES dbo.centro_costo (centro_costo_id),
        CONSTRAINT ck_vale_consumo_estado CHECK (estado IN ('REGISTRADO', 'ANULADO'))
    );
END;
GO

IF OBJECT_ID(N'dbo.detalle_vale_consumo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.detalle_vale_consumo (
        detalle_vale_consumo_id INT IDENTITY(1,1) NOT NULL,
        vale_consumo_id INT NOT NULL,
        producto_id INT NOT NULL,
        precio_unitario DECIMAL(18,4) NOT NULL,
        CONSTRAINT pk_detalle_vale_consumo PRIMARY KEY (detalle_vale_consumo_id),
        CONSTRAINT fk_detalle_vale_vale FOREIGN KEY (vale_consumo_id)
            REFERENCES dbo.vale_consumo (vale_consumo_id),
        CONSTRAINT fk_detalle_vale_producto FOREIGN KEY (producto_id)
            REFERENCES dbo.producto (producto_id),
        CONSTRAINT ck_detalle_vale_precio CHECK (precio_unitario >= 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.distribucion_vale_consumo', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.distribucion_vale_consumo (
        distribucion_vale_consumo_id INT IDENTITY(1,1) NOT NULL,
        detalle_vale_consumo_id INT NOT NULL,
        destino_id INT NOT NULL,
        parte_equipo_id INT NULL,
        cantidad DECIMAL(18,4) NOT NULL,
        CONSTRAINT pk_distribucion_vale_consumo PRIMARY KEY (distribucion_vale_consumo_id),
        CONSTRAINT fk_distribucion_detalle_vale FOREIGN KEY (detalle_vale_consumo_id)
            REFERENCES dbo.detalle_vale_consumo (detalle_vale_consumo_id),
        CONSTRAINT fk_distribucion_destino FOREIGN KEY (destino_id)
            REFERENCES dbo.destino (destino_id),
        CONSTRAINT fk_distribucion_parte_equipo FOREIGN KEY (parte_equipo_id)
            REFERENCES dbo.parte_equipo (parte_equipo_id),
        CONSTRAINT ck_distribucion_cantidad CHECK (cantidad > 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.bitacora', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.bitacora (
        bitacora_id BIGINT IDENTITY(1,1) NOT NULL,
        fecha_hora DATETIME2(0) NOT NULL CONSTRAINT df_bitacora_fecha_hora DEFAULT (SYSDATETIME()),
        usuario_id INT NULL,
        rol_id INT NULL,
        modulo VARCHAR(100) NOT NULL,
        accion VARCHAR(30) NOT NULL,
        detalle VARCHAR(1000) NULL,
        registro_id VARCHAR(50) NULL,
        CONSTRAINT pk_bitacora PRIMARY KEY (bitacora_id),
        CONSTRAINT fk_bitacora_usuario FOREIGN KEY (usuario_id)
            REFERENCES dbo.usuario (usuario_id),
        CONSTRAINT fk_bitacora_rol FOREIGN KEY (rol_id)
            REFERENCES dbo.rol (rol_id),
        CONSTRAINT ck_bitacora_accion CHECK (accion IN ('INICIO_SESION', 'CIERRE_SESION', 'CREAR', 'EDITAR', 'ELIMINAR', 'ACCESO_DENEGADO', 'BLOQUEO_LOGIN'))
    );
END;
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

