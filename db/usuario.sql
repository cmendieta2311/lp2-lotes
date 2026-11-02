
-- ============================================================
-- GESTION DE USUARIOS - VENTA DE LOTES
-- Opcion A: roles fijos
-- Sin relacion con la tabla vendedores
-- ============================================================

BEGIN;

-- 1. Roles disponibles
CREATE TYPE rol_usuario AS ENUM (
    'ADMIN',
    'VENDEDOR',
    'CAJERO'
);

-- 2. Tabla de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol rol_usuario NOT NULL DEFAULT 'VENDEDOR',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    ultimo_acceso TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 3. Indices
CREATE INDEX idx_usuarios_rol
    ON usuarios(rol);

CREATE INDEX idx_usuarios_activo
    ON usuarios(activo);

COMMIT;
