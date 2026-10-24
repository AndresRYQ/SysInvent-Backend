/*
 * V003 - Ingresos, vales, distribuciones, kardex y bitacora.
 * Requiere haber ejecutado V001 y V002.
 */

USE [sysinvent_db];
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

IF OBJECT_ID(N'dbo.kardex_movimiento', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.kardex_movimiento (
        kardex_movimiento_id BIGINT IDENTITY(1,1) NOT NULL,
        vale_consumo_id INT NULL,
        ingreso_almacen_id INT NULL,
        usuario_responsable VARCHAR(100) NOT NULL,
        producto_id INT NOT NULL,
        tipo_movimiento VARCHAR(30) NOT NULL,
        fecha_movimiento DATETIME2(0) NOT NULL,
        cantidad DECIMAL(18,4) NOT NULL,
        saldo_anterior DECIMAL(18,4) NOT NULL,
        saldo_nuevo DECIMAL(18,4) NOT NULL,
        precio_unitario DECIMAL(18,4) NOT NULL,
        centro_costo_id INT NULL,
        destino_id INT NULL,
        parte_equipo_id INT NULL,
        numero_documento VARCHAR(30) NOT NULL,
        CONSTRAINT pk_kardex_movimiento PRIMARY KEY (kardex_movimiento_id),
        CONSTRAINT fk_kardex_vale FOREIGN KEY (vale_consumo_id)
            REFERENCES dbo.vale_consumo (vale_consumo_id),
        CONSTRAINT fk_kardex_ingreso FOREIGN KEY (ingreso_almacen_id)
            REFERENCES dbo.ingreso_almacen (ingreso_almacen_id),
        CONSTRAINT fk_kardex_producto FOREIGN KEY (producto_id)
            REFERENCES dbo.producto (producto_id),
        CONSTRAINT fk_kardex_centro_costo FOREIGN KEY (centro_costo_id)
            REFERENCES dbo.centro_costo (centro_costo_id),
        CONSTRAINT fk_kardex_destino FOREIGN KEY (destino_id)
            REFERENCES dbo.destino (destino_id),
        CONSTRAINT fk_kardex_parte_equipo FOREIGN KEY (parte_equipo_id)
            REFERENCES dbo.parte_equipo (parte_equipo_id),
        CONSTRAINT ck_kardex_tipo_movimiento CHECK
            (tipo_movimiento IN ('ENTRADA_INGRESO', 'SALIDA_VALE', 'AJUSTE_SALIDA', 'AJUSTE_ENTRADA', 'ANULACION_ENTRADA')),
        CONSTRAINT ck_kardex_documento CHECK
            ((vale_consumo_id IS NOT NULL AND ingreso_almacen_id IS NULL)
             OR (vale_consumo_id IS NULL AND ingreso_almacen_id IS NOT NULL)),
        CONSTRAINT ck_kardex_cantidad CHECK (cantidad > 0),
        CONSTRAINT ck_kardex_saldos CHECK (saldo_anterior >= 0 AND saldo_nuevo >= 0)
    );
END;
GO

CREATE OR ALTER TRIGGER dbo.tr_kardex_movimiento_inmutable
ON dbo.kardex_movimiento
AFTER UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    THROW 51000, 'Los movimientos de Kardex son inmutables.', 1;
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
        CONSTRAINT ck_bitacora_accion CHECK (
            accion IN (
                'INICIO_SESION',
                'CIERRE_SESION',
                'CREAR',
                'EDITAR',
                'ELIMINAR',
                'ACCESO_DENEGADO',
                'BLOQUEO_LOGIN'
            )
        )
    );
END;
GO