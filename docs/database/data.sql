-- Datos de prueba para la base de datos
-- API Gestión de Eventos y Asistentes - Grupo 6

-- CATEGORÍAS
INSERT INTO categorias (nombre, descripcion, activo) VALUES
    ('Conferencia', 'Eventos de tipo conferencia con múltiples charlas', TRUE),
    ('Taller', 'Talleres prácticos y capacitaciones', TRUE),
    ('Seminario', 'Seminarios educativos y académicos', TRUE),
    ('Hackathon', 'Competencias de programación y desarrollo', TRUE),
    ('Networking', 'Eventos de networking y socialización', TRUE),
    ('Cultural', 'Eventos culturales y artísticos', TRUE)
    ON CONFLICT (nombre) DO NOTHING;

-- UBICACIONES
INSERT INTO ubicaciones (nombre, direccion, ciudad, departamento, capacidad_maxima, tipo_ubicacion, descripcion, activo) VALUES
    ('Auditorio Principal UES', 'Km 12.5 Carretera a Santa Ana', 'Santa Ana', 'Santa Ana', 500, 'Auditorio', 'Auditorio principal con equipamiento completo', TRUE),
    ('Sala de Conferencias A', 'Edificio Administrativo, 2do piso', 'Santa Ana', 'Santa Ana', 100, 'Sala', 'Sala equipada con proyector y sistema de audio', TRUE),
    ('Campus Central', 'Final 25 Avenida Norte', 'Santa Ana', 'Santa Ana', 1000, 'Campus', 'Espacios abiertos para eventos masivos', TRUE),
    ('Centro de Convenciones', 'Bulevar Venezuela', 'San Salvador', 'San Salvador', 300, 'Centro de Convenciones', 'Moderno centro de convenciones', TRUE),
    ('Laboratorio de Cómputo', 'Edificio de Ingeniería, 1er piso', 'Santa Ana', 'Santa Ana', 40, 'Laboratorio', 'Laboratorio equipado con computadoras', TRUE)
    ON CONFLICT DO NOTHING;

-- USUARIOS
INSERT INTO usuarios (username, password, email, rol, nombre_completo, activo) VALUES
    ('admin', '$2a$10$ABC123...', 'admin@eventos.com', 'ADMIN', 'Administrador del Sistema', TRUE),
    ('organizador1', '$2a$10$DEF456...', 'org1@eventos.com', 'ORGANIZADOR', 'Juan Pérez Organización', TRUE),
    ('organizador2', '$2a$10$GHI789...', 'org2@eventos.com', 'ORGANIZADOR', 'María García Eventos', TRUE),
    ('asistente1', '$2a$10$JKL012...', 'carlos.rodriguez@email.com', 'ASISTENTE', 'Carlos Rodríguez', TRUE),
    ('asistente2', '$2a$10$MNO345...', 'ana.martinez@email.com', 'ASISTENTE', 'Ana Martínez', TRUE),
    ('asistente3', '$2a$10$PQR678...', 'luis.gonzalez@email.com', 'ASISTENTE', 'Luis González', TRUE)
    ON CONFLICT (username) DO NOTHING;

-- ASISTENTES
INSERT INTO asistentes (nombre, apellido, email, telefono, documento_identidad, activo) VALUES
    ('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '7890-1234', '12345678-9', TRUE),
    ('Ana', 'Martínez', 'ana.martinez@email.com', '7890-5678', '98765432-1', TRUE),
    ('Luis', 'González', 'luis.gonzalez@email.com', '7890-9012', '11223344-5', TRUE),
    ('María', 'López', 'maria.lopez@email.com', '7890-3456', '55667788-9', TRUE),
    ('José', 'Hernández', 'jose.hernandez@email.com', '7890-7890', '99887766-5', TRUE),
    ('Laura', 'Jiménez', 'laura.jimenez@email.com', '7890-1122', '44556677-8', TRUE),
    ('Roberto', 'Morales', 'roberto.morales@email.com', '7890-3344', '22334455-6', TRUE),
    ('Patricia', 'Castillo', 'patricia.castillo@email.com', '7890-5566', '66778899-0', TRUE)
    ON CONFLICT (email) DO NOTHING;

-- EVENTOS
INSERT INTO eventos (nombre, descripcion, fecha_evento, duracion_minutos, capacidad_maxima, precio, estado, id_usuario_organizador, id_categoria, id_ubicacion) VALUES
    ('Conferencia de Tecnología 2025', 'Conferencia anual sobre las últimas tendencias en tecnología', '2025-12-15 09:00:00', 480, 200, 25.00, 'ACTIVO', 2, 1, 1),
    ('Workshop de Spring Boot', 'Taller práctico sobre desarrollo con Spring Boot', '2025-11-20 14:00:00', 240, 50, 15.00, 'ACTIVO', 2, 2, 2),
    ('Hackathon Universitario', 'Competencia de programación de 24 horas', '2025-12-01 08:00:00', 1440, 100, 0.00, 'ACTIVO', 2, 4, 3),
    ('Seminario de IA', 'Seminario sobre Inteligencia Artificial y Machine Learning', '2025-11-25 10:00:00', 180, 150, 20.00, 'ACTIVO', 3, 3, 1),
    ('Festival Cultural UES', 'Evento cultural con música y arte', '2025-12-10 16:00:00', 300, 500, 5.00, 'ACTIVO', 3, 6, 3),
    ('Networking Tech Professionals', 'Evento de networking para profesionales de TI', '2025-11-30 18:00:00', 120, 80, 10.00, 'ACTIVO', 2, 5, 4),
    ('Taller de Base de Datos', 'Capacitación en diseño de bases de datos', '2025-12-05 09:00:00', 360, 40, 25.00, 'ACTIVO', 3, 2, 5)
    ON CONFLICT DO NOTHING;

-- REGISTROS DE EVENTOS
INSERT INTO registro_evento (id_evento, id_asistente, estado, precio_pagado, codigo_qr) VALUES
    (1, 1, 'CONFIRMADO', 25.00, 'QR001'),
    (1, 2, 'CONFIRMADO', 25.00, 'QR002'),
    (1, 3, 'CONFIRMADO', 25.00, 'QR003'),
    (2, 1, 'CONFIRMADO', 15.00, 'QR004'),
    (2, 4, 'CONFIRMADO', 15.00, 'QR005'),
    (3, 1, 'ASISTIO', 0.00, 'QR006'),
    (3, 2, 'ASISTIO', 0.00, 'QR007'),
    (3, 5, 'CONFIRMADO', 0.00, 'QR008'),
    (4, 2, 'CONFIRMADO', 20.00, 'QR009'),
    (4, 6, 'CONFIRMADO', 20.00, 'QR010'),
    (5, 3, 'CONFIRMADO', 5.00, 'QR011'),
    (5, 4, 'CONFIRMADO', 5.00, 'QR012'),
    (5, 7, 'CONFIRMADO', 5.00, 'QR013'),
    (6, 5, 'CONFIRMADO', 10.00, 'QR014'),
    (6, 8, 'CONFIRMADO', 10.00, 'QR015'),
    (7, 6, 'CONFIRMADO', 25.00, 'QR016')
    ON CONFLICT DO NOTHING;


-- PAGOS
INSERT INTO pagos (id_registro, monto, metodo_pago, estado_pago, numero_transaccion) VALUES
    (1, 25.00, 'TARJETA', 'COMPLETADO', 'TRX-2024-001'),
    (2, 25.00, 'TRANSFERENCIA', 'COMPLETADO', 'TRX-2024-002'),
    (3, 25.00, 'EFECTIVO', 'COMPLETADO', 'TRX-2024-003'),
    (4, 15.00, 'TARJETA', 'COMPLETADO', 'TRX-2024-004'),
    (5, 15.00, 'EFECTIVO', 'COMPLETADO', 'TRX-2024-005'),
    (6, 0.00, 'OTRO', 'COMPLETADO', 'TRX-2024-006'),
    (7, 0.00, 'OTRO', 'COMPLETADO', 'TRX-2024-007'),
    (8, 0.00, 'OTRO', 'COMPLETADO', 'TRX-2024-008'),
    (9, 20.00, 'TARJETA', 'COMPLETADO', 'TRX-2024-009'),
    (10, 20.00, 'TRANSFERENCIA', 'COMPLETADO', 'TRX-2024-010'),
    (11, 5.00, 'EFECTIVO', 'COMPLETADO', 'TRX-2024-011'),
    (12, 5.00, 'EFECTIVO', 'COMPLETADO', 'TRX-2024-012'),
    (13, 5.00, 'EFECTIVO', 'COMPLETADO', 'TRX-2024-013'),
    (14, 10.00, 'TARJETA', 'COMPLETADO', 'TRX-2024-014'),
    (15, 10.00, 'TARJETA', 'COMPLETADO', 'TRX-2024-015'),
    (16, 25.00, 'TRANSFERENCIA', 'PENDIENTE', 'TRX-2024-016')
    ON CONFLICT DO NOTHING;

-- CALIFICACIONES
INSERT INTO calificaciones (id_evento, id_asistente, puntuacion, comentario) VALUES
    (1, 1, 5, 'Excelente conferencia, muy bien organizada'),
    (1, 2, 4, 'Muy buena, solo mejorar el sonido'),
    (2, 1, 5, 'Taller muy práctico y útil'),
    (3, 1, 5, 'Increíble experiencia, aprendí mucho'),
    (3, 2, 4, 'Buen evento, pero muy largo'),
    (4, 2, 5, 'Seminario de alta calidad'),
    (5, 3, 3, 'Regular, esperaba más actividades')
    ON CONFLICT DO NOTHING;

-- VERIFICACIÓN
SELECT 'Categorías insertadas: ' || COUNT(*) FROM categorias;
SELECT 'Ubicaciones insertadas: ' || COUNT(*) FROM ubicaciones;
SELECT 'Usuarios insertados: ' || COUNT(*) FROM usuarios;
SELECT 'Asistentes insertados: ' || COUNT(*) FROM asistentes;
SELECT 'Eventos insertados: ' || COUNT(*) FROM eventos;
SELECT 'Registros insertados: ' || COUNT(*) FROM registro_evento;
SELECT 'Pagos insertados: ' || COUNT(*) FROM pagos;
SELECT 'Calificaciones insertadas: ' || COUNT(*) FROM calificaciones;