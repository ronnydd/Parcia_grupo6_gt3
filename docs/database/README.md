# Configuración de Base de Datos PostgreSQL

## Requisitos Previos
- PostgreSQL 17 o superior instalado
- Cliente PostgreSQL (pgAdmin)

## Configuración Inicial

### 1. Crear la base de datos

```sql
CREATE DATABASE gestion_eventos;
```

### 2. Ejecutar el script de creación de tablas

```bash
psql -U postgres -d gestion_eventos -f schema.sql
```

O desde pgAdmin:
- Conectar a PostgreSQL
- Abrir Query Tool
- Cargar y ejecutar `schema.sql`

### 3. Cargar datos de prueba (opcional)

```bash
psql -U postgres -d gestion_eventos -f data.sql
```

## Estructura de la Base de Datos

### Tablas Principales:

1. **usuarios** - Usuarios del sistema con roles (ADMIN, ORGANIZADOR, ASISTENTE)
2. **asistentes** - Información de los asistentes a eventos
3. **eventos** - Eventos disponibles para registro
4. **registro_evento** - Relación entre asistentes y eventos (tabla intermedia)

### Relaciones:

- `eventos.id_usuario_organizador` → `usuarios.id_usuario` (N:1)
- `registro_evento.id_evento` → `eventos.id_evento` (N:1)
- `registro_evento.id_asistente` → `asistentes.id_asistente` (N:1)

## Configuración en la Aplicación

### Archivo: `src/main/resources/application.yml`

IMPORTANTE: Configuración del Puerto
El puerto por defecto de PostgreSQL es **5432**, pero puede variar según tu instalación.

### Verificar el puerto donde tienen la conexión:
1. Abrir **pgAdmin**
2. Click derecho en el servidor PostgreSQL
3. Ir a **Properties** → **Connection**
4. Verificar el **Port**

### Actualizar en application.yml:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:PUERTO/gestion_eventos  # Cambiar PUERTO
    username: postgres
    password: PASSWORD  # Cambiar PASSWORD
```
**Puertos comunes:**
- `5432` - Puerto por defecto
- `5433` - Puerto alternativo (segunda instalación)

**IMPORTANTE:** Cambiar el password por el de la instalación de PostgreSQL que cada uno hizo.

## Verificación

Para verificar que todo está configurado correctamente:

```sql
-- Ver todas las tablas
\dt

-- Contar registros
SELECT COUNT(*) FROM usuarios;
SELECT COUNT(*) FROM asistentes;
SELECT COUNT(*) FROM eventos;
SELECT COUNT(*) FROM registro_evento;
```

## Datos de Prueba

El script `data.sql` incluye:
- 3 usuarios (admin, organizador, asistente)
- 5 asistentes
- 5 eventos
- 10 registros de asistentes a eventos

## Troubleshooting

### Error: "database does not exist"
```sql
CREATE DATABASE gestion_eventos;
```

### Error: "role does not exist"
Crear el usuario postgres o cambiar el username en `application.yml`s

### Error de conexión
Verificar que PostgreSQL está corriendo:
```bash
# Windows
services.msc → PostgreSQL
# Linux
sudo service postgresql status
```