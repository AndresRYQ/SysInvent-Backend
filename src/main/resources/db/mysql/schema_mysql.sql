/*
 * SysInvent - Esquema consolidado para MySQL 8.
 *
 * Equivale a las migraciones SQL Server V001..V008 de db/migrations.
 * Es idempotente: puede ejecutarse varias veces sin duplicar objetos ni datos.
 *
 * Ejecución:
 *   mysql -u root -p < src/main/resources/db/mysql/schema_mysql.sql
 */

CREATE DATABASE IF NOT EXISTS sysinvent_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE sysinvent_db;

/* ============================================================
   SEGURIDAD: ROLES, USUARIOS Y ASIGNACIONES (V001)
   ============================================================ */

CREATE TABLE IF NOT EXISTS rol (
    rol_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NULL,
    nombre VARCHAR(255) NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_rol PRIMARY KEY (rol_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NULL,
    usuario VARCHAR(255) NULL,
    nombres VARCHAR(255) NULL,
    ape_paterno VARCHAR(255) NULL,
    ape_materno VARCHAR(255) NULL,
    dni VARCHAR(8) NULL,
    codigo VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    contrasena VARCHAR(255) NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (usuario_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS usuario_rol (
    usuario_rol_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    usuario_id INT NULL,
    rol_id INT NULL,
    activo BOOLEAN NULL,
    fecha_asignacion DATE NULL,
    CONSTRAINT pk_usuario_rol PRIMARY KEY (usuario_rol_id),
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id),
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (rol_id) REFERENCES rol (rol_id)
) ENGINE = InnoDB;

/* ============================================================
   CATÁLOGOS MAESTROS Y PRODUCTOS (V002, V005, V006, V007)
   ============================================================ */

CREATE TABLE IF NOT EXISTS tipo_producto (
    tipo_producto_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_tipo_producto PRIMARY KEY (tipo_producto_id),
    CONSTRAINT uq_tipo_producto_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS categoria (
    categoria_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_categoria PRIMARY KEY (categoria_id),
    CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS unidad_medida (
    unidad_medida_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_unidad_medida PRIMARY KEY (unidad_medida_id),
    CONSTRAINT uq_unidad_medida_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS proveedor (
    proveedor_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    ruc VARCHAR(11) NOT NULL,
    razon_social VARCHAR(255) NOT NULL,
    correo VARCHAR(150) NULL,
    telefono VARCHAR(9) NULL,
    direccion VARCHAR(255) NULL,
    CONSTRAINT pk_proveedor PRIMARY KEY (proveedor_id),
    CONSTRAINT uq_proveedor_ruc UNIQUE (ruc)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS contacto (
    contacto_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    proveedor_id INT NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    cargo VARCHAR(150) NULL,
    telefono VARCHAR(9) NULL,
    correo VARCHAR(150) NULL,
    CONSTRAINT pk_contacto PRIMARY KEY (contacto_id),
    CONSTRAINT fk_contacto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS tipo_documento (
    tipo_documento_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_tipo_documento PRIMARY KEY (tipo_documento_id),
    CONSTRAINT uq_tipo_documento_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS centro_costo (
    centro_costo_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_centro_costo PRIMARY KEY (centro_costo_id),
    CONSTRAINT uq_centro_costo_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS destino (
    destino_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_destino PRIMARY KEY (destino_id),
    CONSTRAINT uq_destino_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS parte_equipo (
    parte_equipo_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(255) NULL,
    CONSTRAINT pk_parte_equipo PRIMARY KEY (parte_equipo_id),
    CONSTRAINT uq_parte_equipo_codigo UNIQUE (codigo)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS producto (
    producto_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NULL,
    tipo_producto_id INT NOT NULL,
    categoria_id INT NOT NULL,
    unidad_medida_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    stock_actual DECIMAL(18,4) NOT NULL DEFAULT 0,
    stock_minimo DECIMAL(18,4) NOT NULL DEFAULT 0,
    precio_unitario DECIMAL(18,4) NOT NULL DEFAULT 0,
    CONSTRAINT pk_producto PRIMARY KEY (producto_id),
    CONSTRAINT uq_producto_codigo UNIQUE (codigo),
    CONSTRAINT fk_producto_tipo_producto FOREIGN KEY (tipo_producto_id) REFERENCES tipo_producto (tipo_producto_id),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (categoria_id),
    CONSTRAINT fk_producto_unidad_medida FOREIGN KEY (unidad_medida_id) REFERENCES unidad_medida (unidad_medida_id),
    CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id),
    CONSTRAINT ck_producto_stock_actual CHECK (stock_actual >= 0),
    CONSTRAINT ck_producto_stock_minimo CHECK (stock_minimo >= 0),
    CONSTRAINT ck_producto_precio_unitario CHECK (precio_unitario >= 0)
) ENGINE = InnoDB;

/* ============================================================
   OPERACIONES DE ALMACÉN, KARDEX Y BITÁCORA (V003)
   ============================================================ */

CREATE TABLE IF NOT EXISTS ingreso_almacen (
    ingreso_almacen_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    numero_ingreso VARCHAR(30) NOT NULL,
    fecha_ingreso DATE NOT NULL,
    proveedor_id INT NOT NULL,
    contacto_id INT NOT NULL,
    tipo_documento_id INT NOT NULL,
    numero_documento VARCHAR(50) NOT NULL,
    observacion VARCHAR(500) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO',
    CONSTRAINT pk_ingreso_almacen PRIMARY KEY (ingreso_almacen_id),
    CONSTRAINT uq_ingreso_almacen_numero UNIQUE (numero_ingreso),
    CONSTRAINT fk_ingreso_almacen_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id),
    CONSTRAINT fk_ingreso_almacen_contacto FOREIGN KEY (contacto_id) REFERENCES contacto (contacto_id),
    CONSTRAINT fk_ingreso_almacen_tipo_documento FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento (tipo_documento_id),
    CONSTRAINT ck_ingreso_almacen_estado CHECK (estado IN ('REGISTRADO', 'ANULADO'))
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_ingreso_almacen (
    detalle_ingreso_almacen_id INT NOT NULL AUTO_INCREMENT,
    ingreso_almacen_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad DECIMAL(18,4) NOT NULL,
    precio_unitario DECIMAL(18,4) NOT NULL,
    CONSTRAINT pk_detalle_ingreso_almacen PRIMARY KEY (detalle_ingreso_almacen_id),
    CONSTRAINT fk_detalle_ingreso_ingreso FOREIGN KEY (ingreso_almacen_id) REFERENCES ingreso_almacen (ingreso_almacen_id),
    CONSTRAINT fk_detalle_ingreso_producto FOREIGN KEY (producto_id) REFERENCES producto (producto_id),
    CONSTRAINT ck_detalle_ingreso_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_detalle_ingreso_precio CHECK (precio_unitario >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS vale_consumo (
    vale_consumo_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    numero_vale VARCHAR(30) NOT NULL,
    fecha_vale DATE NOT NULL,
    centro_costo_id INT NOT NULL,
    solicitante VARCHAR(255) NOT NULL,
    motivo VARCHAR(500) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO',
    CONSTRAINT pk_vale_consumo PRIMARY KEY (vale_consumo_id),
    CONSTRAINT uq_vale_consumo_numero UNIQUE (numero_vale),
    CONSTRAINT fk_vale_consumo_centro_costo FOREIGN KEY (centro_costo_id) REFERENCES centro_costo (centro_costo_id),
    CONSTRAINT ck_vale_consumo_estado CHECK (estado IN ('REGISTRADO', 'ANULADO'))
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_vale_consumo (
    detalle_vale_consumo_id INT NOT NULL AUTO_INCREMENT,
    vale_consumo_id INT NOT NULL,
    producto_id INT NOT NULL,
    precio_unitario DECIMAL(18,4) NOT NULL,
    CONSTRAINT pk_detalle_vale_consumo PRIMARY KEY (detalle_vale_consumo_id),
    CONSTRAINT fk_detalle_vale_vale FOREIGN KEY (vale_consumo_id) REFERENCES vale_consumo (vale_consumo_id),
    CONSTRAINT fk_detalle_vale_producto FOREIGN KEY (producto_id) REFERENCES producto (producto_id),
    CONSTRAINT ck_detalle_vale_precio CHECK (precio_unitario >= 0)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS distribucion_vale_consumo (
    distribucion_vale_consumo_id INT NOT NULL AUTO_INCREMENT,
    detalle_vale_consumo_id INT NOT NULL,
    destino_id INT NOT NULL,
    parte_equipo_id INT NULL,
    cantidad DECIMAL(18,4) NOT NULL,
    CONSTRAINT pk_distribucion_vale_consumo PRIMARY KEY (distribucion_vale_consumo_id),
    CONSTRAINT fk_distribucion_detalle_vale FOREIGN KEY (detalle_vale_consumo_id) REFERENCES detalle_vale_consumo (detalle_vale_consumo_id),
    CONSTRAINT fk_distribucion_destino FOREIGN KEY (destino_id) REFERENCES destino (destino_id),
    CONSTRAINT fk_distribucion_parte_equipo FOREIGN KEY (parte_equipo_id) REFERENCES parte_equipo (parte_equipo_id),
    CONSTRAINT ck_distribucion_cantidad CHECK (cantidad > 0)
) ENGINE = InnoDB;

/*
 * Nota: la regla "un movimiento pertenece a un ingreso o a un vale"
 * (ck_kardex_documento en SQL Server) se implementa con un trigger, porque
 * MySQL restringe los CHECK sobre columnas usadas en claves foráneas.
 */
CREATE TABLE IF NOT EXISTS kardex_movimiento (
    kardex_movimiento_id BIGINT NOT NULL AUTO_INCREMENT,
    vale_consumo_id INT NULL,
    ingreso_almacen_id INT NULL,
    usuario_responsable VARCHAR(100) NOT NULL,
    producto_id INT NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL,
    fecha_movimiento DATETIME NOT NULL,
    cantidad DECIMAL(18,4) NOT NULL,
    saldo_anterior DECIMAL(18,4) NOT NULL,
    saldo_nuevo DECIMAL(18,4) NOT NULL,
    precio_unitario DECIMAL(18,4) NOT NULL,
    centro_costo_id INT NULL,
    destino_id INT NULL,
    parte_equipo_id INT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    CONSTRAINT pk_kardex_movimiento PRIMARY KEY (kardex_movimiento_id),
    CONSTRAINT fk_kardex_vale FOREIGN KEY (vale_consumo_id) REFERENCES vale_consumo (vale_consumo_id),
    CONSTRAINT fk_kardex_ingreso FOREIGN KEY (ingreso_almacen_id) REFERENCES ingreso_almacen (ingreso_almacen_id),
    CONSTRAINT fk_kardex_producto FOREIGN KEY (producto_id) REFERENCES producto (producto_id),
    CONSTRAINT fk_kardex_centro_costo FOREIGN KEY (centro_costo_id) REFERENCES centro_costo (centro_costo_id),
    CONSTRAINT fk_kardex_destino FOREIGN KEY (destino_id) REFERENCES destino (destino_id),
    CONSTRAINT fk_kardex_parte_equipo FOREIGN KEY (parte_equipo_id) REFERENCES parte_equipo (parte_equipo_id),
    CONSTRAINT ck_kardex_tipo_movimiento CHECK
        (tipo_movimiento IN ('ENTRADA_INGRESO', 'SALIDA_VALE', 'AJUSTE_SALIDA', 'AJUSTE_ENTRADA', 'ANULACION_ENTRADA')),
    CONSTRAINT ck_kardex_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_kardex_saldos CHECK (saldo_anterior >= 0 AND saldo_nuevo >= 0)
) ENGINE = InnoDB;

/* Índices de apoyo para el dashboard y los reportes. */
SET @existe_indice := (SELECT COUNT(*) FROM information_schema.statistics
                        WHERE table_schema = DATABASE() AND table_name = 'kardex_movimiento'
                          AND index_name = 'ix_kardex_fecha');
SET @sql := IF(@existe_indice = 0,
               'CREATE INDEX ix_kardex_fecha ON kardex_movimiento (fecha_movimiento)',
               'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

/*
 * Los movimientos del Kardex son inmutables (equivale al trigger
 * tr_kardex_movimiento_inmutable de SQL Server). También se valida que
 * cada movimiento pertenezca a un único documento (ingreso o vale), regla
 * que en SQL Server es un CHECK y que MySQL no admite sobre columnas FK.
 */
DROP TRIGGER IF EXISTS tr_kardex_movimiento_documento;
DROP TRIGGER IF EXISTS tr_kardex_movimiento_no_update;
DROP TRIGGER IF EXISTS tr_kardex_movimiento_no_delete;

DELIMITER $$

CREATE TRIGGER tr_kardex_movimiento_documento
BEFORE INSERT ON kardex_movimiento
FOR EACH ROW
BEGIN
    IF NOT ((NEW.vale_consumo_id IS NOT NULL AND NEW.ingreso_almacen_id IS NULL)
         OR (NEW.vale_consumo_id IS NULL AND NEW.ingreso_almacen_id IS NOT NULL)) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El movimiento de Kardex debe pertenecer a un ingreso o a un vale.';
    END IF;
END$$

CREATE TRIGGER tr_kardex_movimiento_no_update
BEFORE UPDATE ON kardex_movimiento
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Los movimientos de Kardex son inmutables.';
END$$

CREATE TRIGGER tr_kardex_movimiento_no_delete
BEFORE DELETE ON kardex_movimiento
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Los movimientos de Kardex son inmutables.';
END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS bitacora (
    bitacora_id BIGINT NOT NULL AUTO_INCREMENT,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NULL,
    rol_id INT NULL,
    modulo VARCHAR(100) NOT NULL,
    accion VARCHAR(30) NOT NULL,
    detalle VARCHAR(1000) NULL,
    registro_id VARCHAR(50) NULL,
    CONSTRAINT pk_bitacora PRIMARY KEY (bitacora_id),
    CONSTRAINT fk_bitacora_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id),
    CONSTRAINT fk_bitacora_rol FOREIGN KEY (rol_id) REFERENCES rol (rol_id),
    CONSTRAINT ck_bitacora_accion CHECK (accion IN (
        'INICIO_SESION', 'CIERRE_SESION', 'CREAR', 'EDITAR',
        'ELIMINAR', 'ACCESO_DENEGADO', 'BLOQUEO_LOGIN'))
) ENGINE = InnoDB;

/* ============================================================
   MÓDULOS Y PERMISOS (V004)
   ============================================================ */

CREATE TABLE IF NOT EXISTS modulo (
    modulo_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    ruta VARCHAR(255) NULL,
    CONSTRAINT pk_modulo PRIMARY KEY (modulo_id),
    CONSTRAINT uq_modulo_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS rol_modulo (
    rol_modulo_id INT NOT NULL AUTO_INCREMENT,
    fecha_registro DATE NULL,
    fecha_modificacion DATE NULL,
    rol_id INT NOT NULL,
    modulo_id INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_rol_modulo PRIMARY KEY (rol_modulo_id),
    CONSTRAINT uq_rol_modulo UNIQUE (rol_id, modulo_id),
    CONSTRAINT fk_rol_modulo_rol FOREIGN KEY (rol_id) REFERENCES rol (rol_id),
    CONSTRAINT fk_rol_modulo_modulo FOREIGN KEY (modulo_id) REFERENCES modulo (modulo_id)
) ENGINE = InnoDB;

/* ============================================================
   DATOS INICIALES (V004 + V008)
   ============================================================ */

INSERT INTO modulo (fecha_registro, activo, nombre, ruta)
SELECT CURRENT_DATE, TRUE, catalogo.nombre, catalogo.ruta
FROM (
    SELECT 'Roles' AS nombre, '/roles' AS ruta
    UNION ALL SELECT 'Usuarios', '/usuarios'
    UNION ALL SELECT 'Tipos de producto', '/tipos-producto'
    UNION ALL SELECT 'Proveedores', '/proveedores'
    UNION ALL SELECT 'Tipos de documento', '/tipos-documento'
    UNION ALL SELECT 'Destinos', '/destinos'
    UNION ALL SELECT 'Productos', '/productos'
    UNION ALL SELECT 'Unidades de medida', '/unidades-medida'
    UNION ALL SELECT 'Categorías', '/categorias'
    UNION ALL SELECT 'Centros de costo', '/centros-costos'
    UNION ALL SELECT 'Bitácora', '/bitacora'
    UNION ALL SELECT 'Reporte de ingresos', '/reportes/ingresos'
    UNION ALL SELECT 'Reporte de vales', '/reportes/vales'
    UNION ALL SELECT 'Productos más pedidos', '/reportes/productos-mas-pedidos'
    UNION ALL SELECT 'Reporte de Kardex', '/reportes/kardex'
    UNION ALL SELECT 'Contactos', '/contactos'
    UNION ALL SELECT 'Partes de equipo', '/partes-equipo'
    UNION ALL SELECT 'Control de almacén', '/control-almacen'
    UNION ALL SELECT 'Vales de consumo', '/vales-consumo'
    UNION ALL SELECT 'Ingresos de almacén', '/ingresos-almacen'
    UNION ALL SELECT 'Perfil de usuario', '/perfil'
) AS catalogo
WHERE NOT EXISTS (SELECT 1 FROM modulo m WHERE m.nombre = catalogo.nombre);

INSERT INTO rol (fecha_registro, activo, nombre, descripcion)
SELECT CURRENT_DATE, TRUE, 'ADMINISTRADOR', 'Rol con acceso completo al sistema'
WHERE NOT EXISTS (SELECT 1 FROM rol r WHERE r.nombre = 'ADMINISTRADOR');

/* Usuario inicial (mismo hash BCrypt que la migración V004 de SQL Server). */
INSERT INTO usuario (fecha_registro, activo, usuario, nombres, ape_paterno, ape_materno,
                     dni, codigo, email, contrasena)
SELECT CURRENT_DATE, TRUE, '71477205', 'Carlos Jesus', 'Chinchay', 'Pecho',
       '71477205', 'USR001', 'carlos@yopmail.com',
       '$2a$10$Efa0BQyjCWDzNwwTwzcs6.6A2sceUD4fDCZXqDuDYbAjmX4HXlNjK'
WHERE NOT EXISTS (SELECT 1 FROM usuario u WHERE u.usuario = '71477205');

INSERT INTO usuario_rol (fecha_registro, usuario_id, rol_id, activo, fecha_asignacion)
SELECT CURRENT_DATE, u.usuario_id, r.rol_id, TRUE, CURRENT_DATE
FROM usuario u
CROSS JOIN rol r
WHERE u.usuario = '71477205'
  AND r.nombre = 'ADMINISTRADOR'
  AND NOT EXISTS (SELECT 1 FROM usuario_rol ur
                  WHERE ur.usuario_id = u.usuario_id AND ur.rol_id = r.rol_id);

INSERT INTO rol_modulo (fecha_registro, rol_id, modulo_id, activo)
SELECT CURRENT_DATE, r.rol_id, m.modulo_id, TRUE
FROM rol r
CROSS JOIN modulo m
WHERE r.nombre = 'ADMINISTRADOR'
  AND NOT EXISTS (SELECT 1 FROM rol_modulo rm
                  WHERE rm.rol_id = r.rol_id AND rm.modulo_id = m.modulo_id);
