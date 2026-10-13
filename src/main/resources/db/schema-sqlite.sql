PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS rol (
    rol_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER,
    nombre TEXT,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER,
    usuario TEXT,
    nombres TEXT,
    ape_paterno TEXT,
    ape_materno TEXT,
    dni TEXT,
    codigo TEXT,
    email TEXT,
    contrasena TEXT
);

CREATE TABLE IF NOT EXISTS usuario_rol (
    usuario_rol_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    usuario_id INTEGER,
    rol_id INTEGER,
    activo INTEGER,
    fecha_asignacion DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id),
    FOREIGN KEY (rol_id) REFERENCES rol (rol_id)
);

CREATE TABLE IF NOT EXISTS tipo_producto (
    tipo_producto_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS categoria (
    categoria_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS unidad_medida (
    unidad_medida_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS proveedor (
    proveedor_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    ruc TEXT NOT NULL UNIQUE,
    razon_social TEXT NOT NULL,
    correo TEXT,
    telefono TEXT,
    direccion TEXT
);

CREATE TABLE IF NOT EXISTS contacto (
    contacto_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    proveedor_id INTEGER NOT NULL,
    nombre_completo TEXT NOT NULL,
    cargo TEXT,
    telefono TEXT,
    correo TEXT,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id)
);

CREATE TABLE IF NOT EXISTS tipo_documento (
    tipo_documento_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS centro_costo (
    centro_costo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS destino (
    destino_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS parte_equipo (
    parte_equipo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    codigo TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS producto (
    producto_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    codigo TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    tipo_producto_id INTEGER NOT NULL,
    categoria_id INTEGER NOT NULL,
    unidad_medida_id INTEGER NOT NULL,
    proveedor_id INTEGER NOT NULL,
    stock_actual NUMERIC NOT NULL DEFAULT 0 CHECK (stock_actual >= 0),
    stock_minimo NUMERIC NOT NULL DEFAULT 0 CHECK (stock_minimo >= 0),
    precio_unitario NUMERIC NOT NULL DEFAULT 0 CHECK (precio_unitario >= 0),
    FOREIGN KEY (tipo_producto_id) REFERENCES tipo_producto (tipo_producto_id),
    FOREIGN KEY (categoria_id) REFERENCES categoria (categoria_id),
    FOREIGN KEY (unidad_medida_id) REFERENCES unidad_medida (unidad_medida_id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id)
);

CREATE TABLE IF NOT EXISTS ingreso_almacen (
    ingreso_almacen_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    numero_ingreso TEXT NOT NULL UNIQUE,
    fecha_ingreso DATE NOT NULL,
    proveedor_id INTEGER NOT NULL,
    contacto_id INTEGER NOT NULL,
    tipo_documento_id INTEGER NOT NULL,
    numero_documento TEXT NOT NULL,
    observacion TEXT,
    estado TEXT NOT NULL DEFAULT 'REGISTRADO' CHECK (estado IN ('REGISTRADO', 'ANULADO')),
    FOREIGN KEY (proveedor_id) REFERENCES proveedor (proveedor_id),
    FOREIGN KEY (contacto_id) REFERENCES contacto (contacto_id),
    FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento (tipo_documento_id)
);

CREATE TABLE IF NOT EXISTS detalle_ingreso_almacen (
    detalle_ingreso_almacen_id INTEGER PRIMARY KEY AUTOINCREMENT,
    ingreso_almacen_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    cantidad NUMERIC NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC NOT NULL CHECK (precio_unitario >= 0),
    FOREIGN KEY (ingreso_almacen_id) REFERENCES ingreso_almacen (ingreso_almacen_id),
    FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);

CREATE TABLE IF NOT EXISTS vale_consumo (
    vale_consumo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    numero_vale TEXT NOT NULL UNIQUE,
    fecha_vale DATE NOT NULL,
    centro_costo_id INTEGER NOT NULL,
    solicitante TEXT NOT NULL,
    motivo TEXT,
    estado TEXT NOT NULL DEFAULT 'REGISTRADO' CHECK (estado IN ('REGISTRADO', 'ANULADO')),
    FOREIGN KEY (centro_costo_id) REFERENCES centro_costo (centro_costo_id)
);

CREATE TABLE IF NOT EXISTS detalle_vale_consumo (
    detalle_vale_consumo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    vale_consumo_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    precio_unitario NUMERIC NOT NULL CHECK (precio_unitario >= 0),
    FOREIGN KEY (vale_consumo_id) REFERENCES vale_consumo (vale_consumo_id),
    FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
);

CREATE TABLE IF NOT EXISTS distribucion_vale_consumo (
    distribucion_vale_consumo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    detalle_vale_consumo_id INTEGER NOT NULL,
    destino_id INTEGER NOT NULL,
    parte_equipo_id INTEGER,
    cantidad NUMERIC NOT NULL CHECK (cantidad > 0),
    FOREIGN KEY (detalle_vale_consumo_id) REFERENCES detalle_vale_consumo (detalle_vale_consumo_id),
    FOREIGN KEY (destino_id) REFERENCES destino (destino_id),
    FOREIGN KEY (parte_equipo_id) REFERENCES parte_equipo (parte_equipo_id)
);

CREATE TABLE IF NOT EXISTS bitacora (
    bitacora_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_hora TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id INTEGER,
    rol_id INTEGER,
    modulo TEXT NOT NULL,
    accion TEXT NOT NULL CHECK (accion IN ('INICIO_SESION', 'CIERRE_SESION', 'CREAR', 'EDITAR', 'ELIMINAR', 'ACCESO_DENEGADO', 'BLOQUEO_LOGIN')),
    detalle TEXT,
    registro_id TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id),
    FOREIGN KEY (rol_id) REFERENCES rol (rol_id)
);

CREATE TABLE IF NOT EXISTS modulo (
    modulo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    activo INTEGER NOT NULL DEFAULT 1,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT,
    ruta TEXT
);

CREATE TABLE IF NOT EXISTS rol_modulo (
    rol_modulo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_registro DATE,
    fecha_modificacion DATE,
    rol_id INTEGER NOT NULL,
    modulo_id INTEGER NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1,
    UNIQUE (rol_id, modulo_id),
    FOREIGN KEY (rol_id) REFERENCES rol (rol_id),
    FOREIGN KEY (modulo_id) REFERENCES modulo (modulo_id)
);
