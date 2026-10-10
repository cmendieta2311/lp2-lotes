-- =====================================================================
-- BASE DE DATOS: Venta de Lotes (Loteamiento / Fraccionadora)
-- Motor: PostgreSQL
-- Modelo de pago: cuotas fijas + comisiones de vendedores
-- =====================================================================

-- =========================
-- TIPOS ENUMERADOS
-- =========================
CREATE TYPE estado_lote AS ENUM ('disponible', 'reservado', 'vendido', 'inactivo');
CREATE TYPE estado_reserva AS ENUM ('vigente', 'convertida', 'vencida', 'cancelada');
CREATE TYPE estado_venta AS ENUM ('activa', 'cancelada', 'completada');
CREATE TYPE estado_cuota AS ENUM ('pendiente', 'pagada', 'vencida', 'parcial');
CREATE TYPE estado_comision AS ENUM ('pendiente', 'pagada', 'anulada');
CREATE TYPE metodo_pago AS ENUM ('efectivo', 'transferencia', 'cheque', 'tarjeta', 'otro');

-- =========================
-- PROYECTOS / MANZANAS / LOTES
-- =========================
CREATE TABLE proyectos (
    id              SERIAL PRIMARY KEY,
    nombre          VARCHAR(150) NOT NULL,
    ubicacion       VARCHAR(255),
    departamento    VARCHAR(100),
    descripcion     TEXT,
    superficie_total_m2 NUMERIC(12,2),
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT now(),
    activo          BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE manzanas (
    id              SERIAL PRIMARY KEY,
    proyecto_id     INTEGER NOT NULL REFERENCES proyectos(id) ON DELETE CASCADE,
    numero          VARCHAR(20) NOT NULL,
    descripcion     VARCHAR(255),
    UNIQUE (proyecto_id, numero)
);

CREATE TABLE lotes (
    id              SERIAL PRIMARY KEY,
    manzana_id      INTEGER NOT NULL REFERENCES manzanas(id) ON DELETE CASCADE,
    numero_lote     VARCHAR(20) NOT NULL,
    superficie_m2   NUMERIC(10,2) NOT NULL,
    precio_lista    NUMERIC(14,2) NOT NULL,
    padron_catastral VARCHAR(50),
    coordenadas     VARCHAR(100),
    estado          estado_lote NOT NULL DEFAULT 'disponible',
    observaciones   TEXT,
    UNIQUE (manzana_id, numero_lote)
);

CREATE INDEX idx_lotes_estado ON lotes(estado);

-- =========================
-- PERSONAS
-- =========================
CREATE TABLE clientes (
    id              SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    tipo_documento  VARCHAR(20) NOT NULL DEFAULT 'CI',
    numero_documento VARCHAR(30) NOT NULL,
    telefono        VARCHAR(30),
    email           VARCHAR(150),
    direccion       VARCHAR(255),
    fecha_registro  TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (tipo_documento, numero_documento)
);

CREATE TABLE vendedores (
    id                  SERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    apellido            VARCHAR(100) NOT NULL,
    numero_documento    VARCHAR(30) NOT NULL UNIQUE,
    telefono            VARCHAR(30),
    email               VARCHAR(150),
    porcentaje_comision NUMERIC(5,2) NOT NULL DEFAULT 0, -- ej: 5.00 = 5%
    fecha_ingreso       DATE NOT NULL DEFAULT CURRENT_DATE,
    activo              BOOLEAN NOT NULL DEFAULT true
);

-- =========================
-- RESERVAS
-- =========================
CREATE TABLE reservas (
    id              SERIAL PRIMARY KEY,
    lote_id         INTEGER NOT NULL REFERENCES lotes(id),
    cliente_id      INTEGER NOT NULL REFERENCES clientes(id),
    vendedor_id     INTEGER REFERENCES vendedores(id),
    monto_seña      NUMERIC(14,2) NOT NULL,
    fecha_reserva   TIMESTAMP NOT NULL DEFAULT now(),
    fecha_vencimiento DATE NOT NULL,
    estado          estado_reserva NOT NULL DEFAULT 'vigente'
);

CREATE INDEX idx_reservas_lote ON reservas(lote_id);

-- =========================
-- VENTAS
-- =========================
CREATE TABLE ventas (
    id                  SERIAL PRIMARY KEY,
    lote_id             INTEGER NOT NULL UNIQUE REFERENCES lotes(id),
    cliente_id          INTEGER NOT NULL REFERENCES clientes(id),
    vendedor_id         INTEGER REFERENCES vendedores(id),
    reserva_id          INTEGER REFERENCES reservas(id),
    fecha_venta         DATE NOT NULL DEFAULT CURRENT_DATE,
    precio_total         NUMERIC(14,2) NOT NULL,
    monto_entrega        NUMERIC(14,2) NOT NULL DEFAULT 0,
    cantidad_cuotas       INTEGER NOT NULL,
    monto_cuota           NUMERIC(14,2) NOT NULL,
    fecha_primera_cuota   DATE NOT NULL,
    estado               estado_venta NOT NULL DEFAULT 'activa',
    fecha_creacion        TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_ventas_vendedor ON ventas(vendedor_id);

-- =========================
-- PLAN DE PAGOS (CUOTAS)
-- =========================
CREATE TABLE cuotas (
    id              SERIAL PRIMARY KEY,
    venta_id        INTEGER NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
    numero_cuota    INTEGER NOT NULL,
    monto           NUMERIC(14,2) NOT NULL,
    monto_pagado    NUMERIC(14,2) NOT NULL DEFAULT 0,
    fecha_vencimiento DATE NOT NULL,
    estado          estado_cuota NOT NULL DEFAULT 'pendiente',
    UNIQUE (venta_id, numero_cuota)
);

CREATE INDEX idx_cuotas_venta ON cuotas(venta_id);
CREATE INDEX idx_cuotas_estado_venc ON cuotas(estado, fecha_vencimiento);

-- =========================
-- PAGOS
-- =========================
CREATE TABLE pagos (
    id              SERIAL PRIMARY KEY,
    venta_id        INTEGER NOT NULL REFERENCES ventas(id),
    cuota_id        INTEGER REFERENCES cuotas(id), -- puede ser NULL si es un pago a cuenta general
    monto           NUMERIC(14,2) NOT NULL,
    fecha_pago      TIMESTAMP NOT NULL DEFAULT now(),
    metodo_pago     metodo_pago NOT NULL DEFAULT 'efectivo',
    numero_recibo   VARCHAR(30),
    observaciones   TEXT
);

CREATE INDEX idx_pagos_venta ON pagos(venta_id);
CREATE INDEX idx_pagos_cuota ON pagos(cuota_id);

-- =========================
-- COMISIONES DE VENDEDORES
-- =========================
CREATE TABLE comisiones (
    id              SERIAL PRIMARY KEY,
    venta_id        INTEGER NOT NULL UNIQUE REFERENCES ventas(id),
    vendedor_id     INTEGER NOT NULL REFERENCES vendedores(id),
    monto_base      NUMERIC(14,2) NOT NULL,      -- precio_total de la venta al momento del cálculo
    porcentaje      NUMERIC(5,2) NOT NULL,
    monto_comision  NUMERIC(14,2) NOT NULL,
    estado          estado_comision NOT NULL DEFAULT 'pendiente',
    fecha_pago      DATE
);

-- =========================
-- DOCUMENTOS ADJUNTOS
-- =========================
CREATE TABLE documentos (
    id              SERIAL PRIMARY KEY,
    venta_id        INTEGER REFERENCES ventas(id),
    cliente_id      INTEGER REFERENCES clientes(id),
    tipo_documento  VARCHAR(50) NOT NULL, -- ej: 'boleto_compraventa', 'cedula', 'escritura'
    ruta_archivo    VARCHAR(500) NOT NULL,
    fecha_carga     TIMESTAMP NOT NULL DEFAULT now()
);

-- =====================================================================
-- FUNCIONES Y TRIGGERS DE AUTOMATIZACIÓN
-- =====================================================================

-- Al crear una venta: marcar el lote como vendido y generar comisión pendiente
CREATE OR REPLACE FUNCTION fn_venta_insert() RETURNS TRIGGER AS $$
BEGIN
    UPDATE lotes SET estado = 'vendido' WHERE id = NEW.lote_id;

    IF NEW.vendedor_id IS NOT NULL THEN
        INSERT INTO comisiones (venta_id, vendedor_id, monto_base, porcentaje, monto_comision)
        SELECT NEW.id, NEW.vendedor_id, NEW.precio_total, v.porcentaje_comision,
               ROUND(NEW.precio_total * v.porcentaje_comision / 100, 2)
        FROM vendedores v WHERE v.id = NEW.vendedor_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_venta_insert
AFTER INSERT ON ventas
FOR EACH ROW EXECUTE FUNCTION fn_venta_insert();

-- Al registrar un pago: actualizar el monto pagado y estado de la cuota
CREATE OR REPLACE FUNCTION fn_pago_insert() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.cuota_id IS NOT NULL THEN
        UPDATE cuotas
        SET monto_pagado = monto_pagado + NEW.monto,
            estado = CASE
                WHEN monto_pagado + NEW.monto >= monto THEN 'pagada'
                WHEN monto_pagado + NEW.monto > 0 THEN 'parcial'
                ELSE estado
            END
        WHERE id = NEW.cuota_id;
    END IF;

    -- Si todas las cuotas de la venta están pagadas, marcar venta como completada
    UPDATE ventas SET estado = 'completada'
    WHERE id = NEW.venta_id
      AND NOT EXISTS (
          SELECT 1 FROM cuotas
          WHERE venta_id = NEW.venta_id AND estado <> 'pagada'
      );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_pago_insert
AFTER INSERT ON pagos
FOR EACH ROW EXECUTE FUNCTION fn_pago_insert();

-- Marcar cuotas vencidas automáticamente (ejecutar vía cron/job diario)
CREATE OR REPLACE FUNCTION fn_marcar_cuotas_vencidas() RETURNS void AS $$
BEGIN
    UPDATE cuotas
    SET estado = 'vencida'
    WHERE estado = 'pendiente' AND fecha_vencimiento < CURRENT_DATE;
END;
$$ LANGUAGE plpgsql;

-- =====================================================================
-- VISTA ÚTIL: resumen de estado de cuenta por venta
-- =====================================================================
CREATE VIEW vw_estado_cuenta_venta AS
SELECT
    v.id AS venta_id,
    c.nombre || ' ' || c.apellido AS cliente,
    l.numero_lote,
    v.precio_total,
    v.cantidad_cuotas,
    COUNT(cu.id) FILTER (WHERE cu.estado = 'pagada') AS cuotas_pagadas,
    COUNT(cu.id) FILTER (WHERE cu.estado = 'vencida') AS cuotas_vencidas,
    COALESCE(SUM(cu.monto_pagado), 0) AS total_pagado,
    v.precio_total - COALESCE(SUM(cu.monto_pagado), 0) - v.monto_entrega AS saldo_pendiente,
    v.estado AS estado_venta
FROM ventas v
JOIN clientes c ON c.id = v.cliente_id
JOIN lotes l ON l.id = v.lote_id
LEFT JOIN cuotas cu ON cu.venta_id = v.id
GROUP BY v.id, c.nombre, c.apellido, l.numero_lote;
