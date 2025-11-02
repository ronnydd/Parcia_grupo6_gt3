-- Script de creación de base de datos -API Gestión de Eventos y Asistentes - Grupo 6

-- Crear base de datos (ejecutar solo si no existe)
-- CREATE DATABASE gestion_eventos;

-- Conectar a la base de datos
\c gestion_eventos;

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'ORGANIZADOR', 'ASISTENTE')),
    nombre_completo VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_login TIMESTAMP
    );

-- Tabla: asistentes
CREATE TABLE IF NOT EXISTS asistentes (
    id_asistente SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Tabla: eventos
CREATE TABLE IF NOT EXISTS eventos (
    id_evento SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_evento TIMESTAMP NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    capacidad_maxima INTEGER NOT NULL CHECK (capacidad_maxima > 0),
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('ACTIVO', 'CANCELADO', 'FINALIZADO')),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario_organizador INTEGER,
    FOREIGN KEY (id_usuario_organizador) REFERENCES usuarios(id_usuario) ON DELETE SET NULL
    );

-- Tabla: registro_evento (relación entre asistentes y eventos)
CREATE TABLE IF NOT EXISTS registro_evento (
   id_registro SERIAL PRIMARY KEY,
   id_evento INTEGER NOT NULL,
   id_asistente INTEGER NOT NULL,
   fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   estado VARCHAR(20) NOT NULL CHECK (estado IN ('CONFIRMADO', 'CANCELADO', 'ASISTIO', 'NO_ASISTIO')),
    precio_pagado DECIMAL(10, 2) NOT NULL CHECK (precio_pagado >= 0),
    codigo_qr VARCHAR(10) UNIQUE,
    fecha_checkin TIMESTAMP,
    fecha_cancelacion TIMESTAMP,
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE,
    FOREIGN KEY (id_asistente) REFERENCES asistentes(id_asistente) ON DELETE CASCADE,
    UNIQUE (id_evento, id_asistente)
    );

-- Índices para mejorar el rendimiento
CREATE INDEX idx_eventos_fecha ON eventos(fecha_evento);
CREATE INDEX idx_eventos_estado ON eventos(estado);
CREATE INDEX idx_registro_evento_evento ON registro_evento(id_evento);
CREATE INDEX idx_registro_evento_asistente ON registro_evento(id_asistente);

-- Comentarios en las tablas
COMMENT ON TABLE usuarios IS 'Usuarios del sistema con roles';
COMMENT ON TABLE asistentes IS 'Asistentes registrados en el sistema';
COMMENT ON TABLE eventos IS 'Eventos disponibles para registro';
COMMENT ON TABLE registro_evento IS 'Registros de asistentes a eventos';