# Configuración de Base de Datos PostgreSQL

## Requisitos Previos
- PostgreSQL 17 o superior instalado
- Cliente PostgreSQL (pgAdmin o similar)

---

## Configuración Inicial

### 1. Crear la base de datos

```sql
CREATE DATABASE gestion_eventos;
```

### 2. Ejecutar el script de creación de tablas

**Opción A - Desde terminal:**
```bash
psql -U postgres -d gestion_eventos -f schema.sql
```

**Opción B - Desde pgAdmin:**
1. Conectar a PostgreSQL
2. Abrir **Query Tool**
3. Cargar y ejecutar `schema.sql`

### 3. Cargar datos de prueba (opcional)

```bash
psql -U postgres -d gestion_eventos -f data.sql
```

---

## Estructura de la Base de Datos

### Tablas Principales (8 entidades):

1. **categorias** - Categorías/tipos de eventos (Conferencia, Taller, Seminario, etc.)
2. **ubicaciones** - Sedes físicas donde se realizan eventos
3. **usuarios** - Usuarios del sistema con roles (ADMIN, ORGANIZADOR, ASISTENTE)
4. **asistentes** - Asistentes registrados en el sistema
5. **eventos** - Eventos disponibles para registro
6. **registro_evento** - Relación entre asistentes y eventos (inscripciones)
7. **pagos** - Transacciones de pago de registros
8. **calificaciones** - Calificaciones y reseñas de eventos por asistentes

---

## Relaciones Entre Tablas

### Relaciones Principales:

**Eventos:**
- `eventos.id_categoria` → `categorias.id_categoria` (N:1)
- `eventos.id_ubicacion` → `ubicaciones.id_ubicacion` (N:1)
- `eventos.id_usuario_organizador` → `usuarios.id_usuario` (N:1)

**Registros:**
- `registro_evento.id_evento` → `eventos.id_evento` (N:1)
- `registro_evento.id_asistente` → `asistentes.id_asistente` (N:1)

**Pagos:**
- `pagos.id_registro` → `registro_evento.id_registro` (1:1)

**Calificaciones:**
- `calificaciones.id_evento` → `eventos.id_evento` (N:1)
- `calificaciones.id_asistente` → `asistentes.id_asistente` (N:1)

**Asistentes:**
- `asistentes.id_usuario` → `usuarios.id_usuario` (1:1) - Opcional

---

## Configuración en la Aplicación

### Archivo: `src/main/resources/application.yml`

### IMPORTANTE: Configuración del Puerto

El puerto por defecto de PostgreSQL es **5432**, pero puede variar según tu instalación.

#### Verificar el puerto de conexión:
1. Abrir **pgAdmin**
2. Click derecho en el servidor PostgreSQL
3. Ir a **Properties** → **Connection**
4. Verificar el **Port**

#### Actualizar en `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:PUERTO/gestion_eventos  # Cambiar PUERTO
    username: postgres                                        # Cambiar si es necesario
    password: TU_PASSWORD                                     # IMPORTANTE: Cambiar PASSWORD
    driver-class-name: org.postgresql.Driver
```

**Puertos comunes:**
- `5432` - Puerto por defecto
- `5433` - Puerto alternativo (segunda instalación)

**CRÍTICO:** Cada miembro del equipo debe cambiar el `password` por el de su instalación local de PostgreSQL.

---

## Verificación

Para verificar que todo está configurado correctamente:

```sql
-- Ver todas las tablas
\dt

-- Contar registros en cada tabla
SELECT 'Categorías: ' || COUNT(*) FROM categorias;
SELECT 'Ubicaciones: ' || COUNT(*) FROM ubicaciones;
SELECT 'Usuarios: ' || COUNT(*) FROM usuarios;
SELECT 'Asistentes: ' || COUNT(*) FROM asistentes;
SELECT 'Eventos: ' || COUNT(*) FROM eventos;
SELECT 'Registros: ' || COUNT(*) FROM registro_evento;
SELECT 'Pagos: ' || COUNT(*) FROM pagos;
SELECT 'Calificaciones: ' || COUNT(*) FROM calificaciones;
```

---

## Datos de Prueba

El script `data.sql` incluye datos completos para todas las tablas:

| Tabla | Cantidad | Descripción |
|-------|----------|-------------|
| **categorias** | 6 | Conferencia, Taller, Seminario, Hackathon, Networking, Cultural |
| **ubicaciones** | 5 | Auditorios, salas, campus, laboratorios |
| **usuarios** | 6 | 1 admin, 2 organizadores, 3 asistentes |
| **asistentes** | 8 | Personas registradas como asistentes |
| **eventos** | 7 | Eventos de diferentes tipos y fechas |
| **registro_evento** | 16 | Inscripciones de asistentes a eventos |
| **pagos** | 16 | Transacciones de pago (completadas y pendientes) |
| **calificaciones** | 7 | Reseñas de eventos por asistentes |

### Datos Importantes de Prueba:

**Usuarios de prueba:**
- `admin` / `admin@eventos.com` - Rol: ADMIN
- `organizador1` / `org1@eventos.com` - Rol: ORGANIZADOR
- `organizador2` / `org2@eventos.com` - Rol: ORGANIZADOR

**Eventos de prueba:**
- Conferencia de Tecnología 2025 (200 personas, $25.00)
- Workshop de Spring Boot (50 personas, $15.00)
- Hackathon Universitario (100 personas, GRATIS)

---

## Detalles de las Tablas

### 1. **categorias**
```sql
- id_categoria (PK)
- nombre (UNIQUE)
- descripcion
- activo
- fecha_creacion
```

### 2. **ubicaciones**
```sql
- id_ubicacion (PK)
- nombre
- direccion
- ciudad
- departamento
- capacidad_maxima
- tipo_ubicacion
- descripcion
- activo
- fecha_creacion
```

### 3. **usuarios**
```sql
- id_usuario (PK)
- username (UNIQUE)
- password
- email (UNIQUE)
- rol (CHECK: ADMIN, ORGANIZADOR, ASISTENTE)
- nombre_completo
- activo
- fecha_creacion
- ultimo_login
```

### 4. **asistentes**
```sql
- id_asistente (PK)
- id_usuario (FK - UNIQUE - Opcional)
- nombre
- apellido
- email (UNIQUE)
- telefono
- documento_identidad (UNIQUE)
- activo
- fecha_registro
- fecha_actualizacion
```

### 5. **eventos**
```sql
- id_evento (PK)
- nombre
- descripcion
- fecha_evento
- duracion_minutos
- capacidad_maxima
- precio
- estado (CHECK: ACTIVO, CANCELADO, FINALIZADO)
- fecha_creacion
- fecha_actualizacion
- id_usuario_organizador (FK)
- id_categoria (FK)
- id_ubicacion (FK)
```

### 6. **registro_evento**
```sql
- id_registro (PK)
- id_evento (FK)
- id_asistente (FK)
- fecha_registro
- estado (CHECK: CONFIRMADO, CANCELADO, ASISTIO, NO_ASISTIO)
- precio_pagado
- codigo_qr (UNIQUE)
- fecha_checkin
- fecha_cancelacion
- UNIQUE(id_evento, id_asistente)
```

### 7. **pagos**
```sql
- id_pago (PK)
- id_registro (FK - UNIQUE)
- monto
- metodo_pago (CHECK: EFECTIVO, TARJETA, TRANSFERENCIA, OTRO)
- estado_pago (CHECK: PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO)
- numero_transaccion (UNIQUE)
- fecha_pago
- comprobante_url
```

### 8. **calificaciones**
```sql
- id_calificacion (PK)
- id_evento (FK)
- id_asistente (FK)
- puntuacion (CHECK: 1-5)
- comentario
- fecha_calificacion
- UNIQUE(id_evento, id_asistente)
```

---

## Índices para Optimización

El script `schema.sql` incluye índices para mejorar el rendimiento:

```sql
- idx_eventos_fecha          -- Búsquedas por fecha
- idx_eventos_estado         -- Filtros por estado
- idx_eventos_categoria      -- Filtros por categoría
- idx_eventos_ubicacion      -- Filtros por ubicación
- idx_registro_evento_evento -- Consultas de registros por evento
- idx_registro_evento_asistente -- Consultas de registros por asistente
- idx_pagos_estado          -- Filtros de pagos por estado
- idx_calificaciones_evento -- Consultas de calificaciones por evento
```

---

## Troubleshooting

### Error: "database does not exist"
**Solución:**
```sql
CREATE DATABASE gestion_eventos;
```

### Error: "role does not exist"
**Solución:** Crear el usuario postgres o cambiar el username en `application.yml`

### Error de conexión: "Connection refused"
**Causa:** PostgreSQL no está corriendo

**Solución:**
```bash
# Windows
services.msc → Buscar PostgreSQL → Iniciar servicio

# Linux
sudo service postgresql status
sudo service postgresql start
```

### Error: "relation does not exist"
**Causa:** Las tablas no han sido creadas

**Solución:** Ejecutar `schema.sql`

### Error: "port is already in use"
**Causa:** El puerto 8080 ya está siendo usado por otra aplicación

**Solución:** Cambiar el puerto en `application.yml`:
```yaml
server:
  port: 8081  # Cambiar a otro puerto disponible
```

### Error: "duplicate key value violates unique constraint"
**Causa:** Los datos de prueba ya fueron insertados

**Solución:**
- Opción 1: Borrar los datos existentes
- Opción 2: No ejecutar `data.sql` nuevamente
- Opción 3: Recrear la base de datos desde cero

---

## Recrear la Base de Datos Desde Cero

Si necesitas empezar de nuevo:

```sql
-- ADVERTENCIA: Esto eliminará TODOS los datos

-- 1. Desconectar todas las conexiones activas
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'gestion_eventos'
  AND pid <> pg_backend_pid();

-- 2. Eliminar la base de datos
DROP DATABASE IF EXISTS gestion_eventos;

-- 3. Crear nuevamente
CREATE DATABASE gestion_eventos;

-- 4. Conectar y ejecutar schema.sql
\c gestion_eventos;
\i schema.sql

-- 5. (Opcional) Cargar datos de prueba
\i data.sql
```

---

## Soporte

Para problemas con la configuración de la base de datos, contactar a cualquier miembro del equipo o revisar la documentación oficial de PostgreSQL.

---

**Última actualización:** Noviembre 2025  
**Proyecto:** API Gestión de Eventos - Grupo 6  
**Universidad de El Salvador** - Facultad Multidisciplinaria de Occidente