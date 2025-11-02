-- Datos de prueba para la base de datos

-- Insertar usuarios
INSERT INTO usuarios (username, password, email, rol, nombre_completo, activo) VALUES
   ('admin', '$2a$10$ABC123...', 'admin@eventos.com', 'ADMIN', 'Administrador del Sistema', TRUE),
   ('organizador1', '$2a$10$DEF456...', 'org1@eventos.com', 'ORGANIZADOR', 'Juan Pérez', TRUE),
   ('asistente1', '$2a$10$GHI789...', 'user1@eventos.com', 'ASISTENTE', 'María García', TRUE)
    ON CONFLICT (username) DO NOTHING;

-- Insertar asistentes
INSERT INTO asistentes (nombre, apellido, email, telefono, documento_identidad, activo) VALUES
    ('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '7890-1234', '12345678-9', TRUE),
    ('Ana', 'Martínez', 'ana.martinez@email.com', '7890-5678', '98765432-1', TRUE),
    ('Luis', 'González', 'luis.gonzalez@email.com', '7890-9012', '11223344-5', TRUE),
    ('María', 'López', 'maria.lopez@email.com', '7890-3456', '55667788-9', TRUE),
    ('José', 'Hernández', 'jose.hernandez@email.com', '7890-7890', '99887766-5', TRUE)
    ON CONFLICT (email) DO NOTHING;

-- Insertar eventos
INSERT INTO eventos (nombre, descripcion, fecha_evento, ubicacion, capacidad_maxima, precio, estado, id_usuario_organizador) VALUES
 ('Conferencia de Tecnología 2025', 'Conferencia anual sobre las últimas tendencias en tecnología', '2025-12-15 09:00:00', 'Auditorio UES', 200, 25.00, 'ACTIVO', 2),
 ('Workshop de Spring Boot', 'Taller práctico sobre desarrollo con Spring Boot', '2025-11-20 14:00:00', 'Sala de Conferencias A', 50, 15.00, 'ACTIVO', 2),
 ('Hackathon Universitario', 'Competencia de programación de 24 horas', '2025-12-01 08:00:00', 'Campus Central', 100, 0.00, 'ACTIVO', 2),
 ('Seminario de IA', 'Seminario sobre Inteligencia Artificial y Machine Learning', '2025-11-25 10:00:00', 'Auditorio Principal', 150, 20.00, 'ACTIVO', 2),
 ('Festival Cultural', 'Evento cultural con música y arte', '2025-12-10 16:00:00', 'Plaza Central', 500, 5.00, 'ACTIVO', 2)
    ON CONFLICT DO NOTHING;

-- Insertar registros de eventos (asistentes registrados a eventos)
INSERT INTO registro_evento (id_evento, id_asistente, estado, precio_pagado, codigo_qr) VALUES
    (1, 1, 'CONFIRMADO', 25.00, 'QR001'),
    (1, 2, 'CONFIRMADO', 25.00, 'QR002'),
    (2, 1, 'CONFIRMADO', 15.00, 'QR003'),
    (2, 3, 'CONFIRMADO', 15.00, 'QR004'),
    (3, 1, 'CONFIRMADO', 0.00, 'QR005'),
    (3, 2, 'CONFIRMADO', 0.00, 'QR006'),
    (3, 4, 'CONFIRMADO', 0.00, 'QR007'),
    (4, 2, 'CONFIRMADO', 20.00, 'QR008'),
    (4, 5, 'CONFIRMADO', 20.00, 'QR009'),
    (5, 3, 'CONFIRMADO', 5.00, 'QR010')
    ON CONFLICT DO NOTHING;

-- Verificar los datos insertados
SELECT 'Usuarios insertados: ' || COUNT(*) FROM usuarios;
SELECT 'Asistentes insertados: ' || COUNT(*) FROM asistentes;
SELECT 'Eventos insertados: ' || COUNT(*) FROM eventos;
SELECT 'Registros insertados: ' || COUNT(*) FROM registro_evento;