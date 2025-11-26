# API REST - Sistema de Gestión de Eventos y Asistentes

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema completo para la gestión integral de eventos, asistentes, inscripciones, pagos y calificaciones, desarrollado con **Spring Boot** siguiendo las mejores prácticas de arquitectura en capas.

---

## Tabla de Contenidos

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Características Principales](#-características-principales)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Modelo de Datos](#-modelo-de-datos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Pruebas](#-pruebas)
- [Equipo de Desarrollo](#-equipo-de-desarrollo)

---

## Descripción del Proyecto

**API REST** desarrollada como proyecto de la asignatura **Programación Orientada a Objetos** de la carrera de **Ingeniería en Desarrollo de Software** de la Universidad de El Salvador, Facultad Multidisciplinaria de Occidente.

El sistema permite la **gestión completa del ciclo de vida de eventos**, desde su creación hasta la calificación posterior por parte de los asistentes, incluyendo:

- Gestión de usuarios con diferentes roles
- Creación y administración de eventos
- Sistema de inscripciones con control de capacidad
- Procesamiento de pagos con múltiples métodos
- Sistema de calificaciones y reseñas
- Check-in mediante códigos QR únicos

---

## Características Principales

### Sistema de Usuarios y Roles
- **3 Roles de Usuario**: ADMIN, ORGANIZADOR, ASISTENTE
- Gestión de perfiles y autenticación
- Control de permisos por rol

### Gestión de Eventos
- Creación de eventos con información detallada
- Categorización y ubicación de eventos
- Control de capacidad máxima
- Estados: ACTIVO, CANCELADO, FINALIZADO
- Validación de fechas futuras

### Registro de Asistentes
- Inscripción con validación de capacidad
- Generación automática de códigos QR únicos
- Estados: CONFIRMADO, ASISTIO, CANCELADO, NO_ASISTIO
- Check-in mediante código QR

### Sistema de Pagos
- Múltiples métodos: EFECTIVO, TARJETA, TRANSFERENCIA
- Estados: PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO
- Generación automática de números de transacción
- Cálculo de totales

### Calificaciones
- Sistema de puntuación (1-5 estrellas)
- Comentarios opcionales
- Cálculo automático de promedios
- Solo asistentes verificados pueden calificar

### Gestión de Ubicaciones y Categorías
- Ubicaciones con capacidades específicas
- Categorías para clasificación de eventos
- Múltiples tipos de ubicaciones

---

## Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **Lombok** - Reducción de código boilerplate
- **Bean Validation** - Validaciones de datos

### Base de Datos
- **PostgreSQL 17** - Base de datos relacional
- **HikariCP** - Pool de conexiones

### Testing
- **JUnit 5** - Framework de pruebas
- **Mockito** - Mocking para tests unitarios
- **80+ Pruebas Unitarias** con cobertura completa

### Herramientas de Desarrollo
- **Maven/Gradle** - Gestión de dependencias
- **IntelliJ IDEA** - IDE principal
- **Postman** - Pruebas de API
- **Git/GitHub** - Control de versiones

---

## Arquitectura del Sistema

### Patrón de Arquitectura en Capas

```
┌─────────────────────────────────────────┐
│          CAPA DE PRESENTACIÓN           │
│    (Controllers REST - @RestController) │
├─────────────────────────────────────────┤
│         CAPA DE LÓGICA DE NEGOCIO       │
│        (Services - @Service)            │
├─────────────────────────────────────────┤
│        CAPA DE PERSISTENCIA             │
│   (Repositories - @Repository + JPA)    │
├─────────────────────────────────────────┤
│           BASE DE DATOS                 │
│          (PostgreSQL 17)                │
└─────────────────────────────────────────┘
```

### Componentes Principales

#### DTOs (Data Transfer Objects)
- Desacoplamiento entre capas
- Validaciones con Bean Validation
- Transformación de datos

#### Services
- Lógica de negocio centralizada
- Validaciones complejas
- Gestión de transacciones

#### Repositories
- Acceso a datos mediante Spring Data JPA
- Consultas personalizadas con @Query
- Métodos derivados de nombres

#### Exception Handler
- Manejo centralizado de excepciones
- Códigos HTTP apropiados (404, 400, 409, 500)
- Respuestas de error consistentes

---

## Modelo de Datos

### Entidades Principales

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   Usuario    │◄──────│    Evento    │──────►│  Categoría   │
└──────────────┘       └──────────────┘       └──────────────┘
                              │
                              │
                              ▼
                       ┌──────────────┐
                       │  Ubicación   │
                       └──────────────┘
                              │
                              │
       ┌──────────────────────┼──────────────────────┐
       │                      │                      │
       ▼                      ▼                      ▼
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│  Asistente   │◄─────►│   Registro   │──────►│     Pago     │
└──────────────┘       │    Evento    │       └──────────────┘
       │               └──────────────┘
       │                      │
       │                      │
       └──────────┬───────────┘
                  │
                  ▼
           ┌──────────────┐
           │ Calificación │
           └──────────────┘
```

### 8 Entidades Principales

1. **Usuario** - Usuarios del sistema con roles
2. **Asistente** - Información de participantes
3. **Evento** - Eventos disponibles
4. **Categoría** - Tipos de eventos
5. **Ubicación** - Sedes físicas
6. **RegistroEvento** - Inscripciones (tabla intermedia)
7. **Pago** - Transacciones de pago
8. **Calificación** - Reseñas de eventos

### Relaciones Clave

- `Evento` **N:1** `Usuario` (organizador)
- `Evento` **N:1** `Categoría`
- `Evento` **N:1** `Ubicación`
- `RegistroEvento` **N:1** `Evento`
- `RegistroEvento` **N:1** `Asistente`
- `Pago` **1:1** `RegistroEvento`
- `Calificación` **N:1** `Evento`
- `Calificación` **N:1** `Asistente`

---

## Instalación y Configuración

### Requisitos Previos

- **Java 17** o superior
- **PostgreSQL 17** o superior
- **Maven** o **Gradle**
- **IntelliJ IDEA** (recomendado)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/ronnydd/Parcia_grupo6_gt3.git
cd Parcia_grupo6_gt3
```

### 2. Configurar PostgreSQL

#### 2.1 Crear la Base de Datos

```sql
CREATE DATABASE gestion_eventos;
```

#### 2.2 Ejecutar el Script de Tablas

**Opción A - Desde terminal:**
```bash
psql -U postgres -d gestion_eventos -f schema.sql
```

**Opción B - Desde pgAdmin:**
1. Conectar a PostgreSQL
2. Abrir **Query Tool**
3. Cargar y ejecutar `schema.sql`

#### 2.3 Cargar Datos de Prueba (Opcional)

```bash
psql -U postgres -d gestion_eventos -f data.sql
```

Los datos de prueba incluyen:
- 6 categorías predefinidas
- 5 ubicaciones
- 6 usuarios (admin, organizadores, asistentes)
- 8 asistentes
- 7 eventos
- 16 registros
- 16 pagos
- 7 calificaciones

### 3. Configurar la Aplicación

#### 3.1 Verificar el Puerto de PostgreSQL

1. Abrir **pgAdmin**
2. Click derecho en el servidor → **Properties** → **Connection**
3. Verificar el **Port** (usualmente `5432` o `5433`)

#### 3.2 Actualizar `application.yml`

**Ubicación:** `src/main/resources/application.yml`

```yaml
# Configuración del servidor
server:
  port: 8080
  servlet:
    context-path: /api/v1

spring:
  # Configuración de la base de datos
  datasource:
    url: jdbc:postgresql://localhost:5432/gestion_eventos  # Cambiar puerto si es necesario
    username: postgres                                      # Cambiar usuario
    password: tu_password                                   # Cambiar password
    driver-class-name: org.postgresql.Driver

  # Configuración de JPA/Hibernate
  jpa:
    hibernate:
      ddl-auto: update                    # Opciones: create, update, validate
    show-sql: true                        # Mostrar SQL en consola
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

**IMPORTANTE:**
- Cambiar `5432` por el puerto correcto de tu instalación
- Cambiar `tu_password` por tu contraseña de PostgreSQL

---

## Ejecución del Proyecto

### Opción 1: Desde IntelliJ IDEA

1. Abrir el proyecto en IntelliJ
2. Esperar a que Maven/Gradle descargue las dependencias
3. Ejecutar la clase `ApiGestionEventosApplication.java`
4. La aplicación estará disponible en: `http://localhost:8080/api/v1`

### Opción 2: Desde Terminal

**Con Maven:**
```bash
./mvnw spring-boot:run
```

**Con Gradle:**
```bash
./gradlew bootRun
```

### Verificar que está corriendo

Deberías ver en la consola:
```
Started ApiGestionEventosApplication in X.XXX seconds
```

---

## Endpoints de la API

### Base URL
```
http://localhost:8080/api/v1
```

### Resumen de Endpoints

| Módulo | Endpoints | Descripción |
|--------|-----------|-------------|
| **Categorías** | 13        | CRUD de categorías de eventos |
| **Ubicaciones** | 15        | Gestión de sedes y lugares |
| **Usuarios** | 16        | Administración de usuarios y roles |
| **Asistentes** | 11        | Registro de asistentes |
| **Eventos** | 17        | Gestión completa de eventos |
| **Registros** | 18        | Inscripciones y check-in |
| **Pagos** | 20        | Procesamiento de pagos |
| **Calificaciones** | 15        | Sistema de reseñas |
| **TOTAL** | **125**   | **endpoints funcionales** |

### Ejemplos de Endpoints Principales

#### Categorías
```
GET    /categorias              - Listar todas
GET    /categorias/{id}         - Obtener por ID
POST   /categorias              - Crear nueva
PUT    /categorias/{id}         - Actualizar
DELETE /categorias/{id}         - Eliminar
```

#### Eventos
```
GET    /eventos                    - Listar todos
GET    /eventos/{id}               - Obtener por ID
GET    /eventos/activos-proximos   - Eventos próximos activos
GET    /eventos/{id}/disponibilidad - Verificar capacidad
POST   /eventos                    - Crear evento
PUT    /eventos/{id}               - Actualizar
PATCH  /eventos/{id}/estado        - Cambiar estado
DELETE /eventos/{id}               - Eliminar
```

#### Registros (Inscripciones)
```
POST   /registros                      - Inscribir asistente
GET    /registros/evento/{id}          - Por evento
PATCH  /registros/{id}/checkin         - Realizar check-in
PATCH  /registros/checkin-qr/{codigo}  - Check-in por QR
PATCH  /registros/{id}/cancelar        - Cancelar inscripción
```

#### Pagos
```
POST   /pagos                   - Registrar pago
GET    /pagos/total-completados - Total de ingresos
PATCH  /pagos/{id}/confirmar    - Confirmar pago
PATCH  /pagos/{id}/rechazar     - Rechazar pago
PATCH  /pagos/{id}/reembolsar   - Procesar reembolso
```

### Colección de Postman

La colección completa de Postman con todos los endpoints está disponible en:
```
postman/API_Gestion_Eventos.postman_collection.json
```

**Para importar:**
1. Abrir Postman
2. Click en **Import**
3. Seleccionar el archivo JSON
4. Importar también el archivo de variables de entorno

---

## Pruebas

### Pruebas Unitarias con JUnit 5

El proyecto cuenta con **80+ pruebas unitarias** que cubren:
- Todos los servicios (8 servicios completos)
- Casos exitosos y validaciones
- Manejo de excepciones
- Reglas de negocio

### Estructura de Tests

```
src/test/java/com/eventos/api/service/
├── AsistenteServiceTest.java       (12 tests)
├── EventoServiceTest.java          (12 tests)
├── UsuarioServiceTest.java         (11 tests)
├── RegistroEventoServiceTest.java  (12 tests)
├── CategoriaServiceTest.java       (11 tests)
├── UbicacionServiceTest.java       (17 tests)
├── PagoServiceTest.java            (10 tests)
└── CalificacionServiceTest.java    (10 tests)
```

### Ejecutar Pruebas

**Con Maven:**
```bash
./mvnw test
```

**Con Gradle:**
```bash
./gradlew test
```

**Desde IntelliJ:**
- Click derecho en `src/test` → **Run 'All Tests'**

### Cobertura de Pruebas

- **Servicios:** 100% cubiertos
- **Casos de éxito:** 
- **Validaciones:** 
- **Excepciones:** 
- **Reglas de negocio:** 

---

## Estructura del Proyecto

```
Parcia_grupo6_gt3/
├── src/
│   ├── main/
│   │   ├── java/com/eventos/api/
│   │   │   ├── controller/          # Controladores REST
│   │   │   │   ├── AsistenteController.java
│   │   │   │   ├── EventoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── RegistroEventoController.java
│   │   │   │   ├── CategoriaController.java
│   │   │   │   ├── UbicacionController.java
│   │   │   │   ├── PagoController.java
│   │   │   │   └── CalificacionController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   └── ...DTO.java
│   │   │   ├── entity/              # Entidades JPA
│   │   │   │   ├── enums/          # Enumeraciones
│   │   │   │   └── ...Entity.java
│   │   │   ├── repository/          # Repositorios
│   │   │   │   └── ...Repository.java
│   │   │   ├── service/             # Servicios
│   │   │   │   └── ...Service.java
│   │   │   ├── exception/           # Manejo de excepciones
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── ApiGestionEventosApplication.java
│   │   └── resources/
│   │       ├── application.yml      # Configuración
│   │       ├── schema.sql          # Script de tablas
│   │       └── data.sql            # Datos de prueba
│   └── test/
│       └── java/com/eventos/api/
│           └── service/             # Tests unitarios
│               └── ...ServiceTest.java
├── postman/                         # Colección de Postman
│   ├── API_Gestion_Eventos.postman_collection.json
│   └── Eventos_Environment.postman_environment.json
├── docs/                            # Documentación
│   ├── diagramas/
│   └── casos-uso/
├── pom.xml / build.gradle          # Dependencias
└── README.md                       # Este archivo
```

---

## Equipo de Desarrollo

### Grupo 6 - Programación Orientada a Objetos

| Nombre | Carnet | Rol |
|--------|--------|-----|
| Esdras Leonel Peraza Pérez | PP24012 | Desarrollador |
| Ana Cristina Martínez Salas | MS24088 | Desarrolladora |
| Rodrigo Alexis Mercado Calidonio | MC24029 | Desarrollador |
| Ronny Xavier Durán Delgado | DD23010 | Desarrollador |
| Jose Fabricio Reyes Sermeño | RS24033 | Desarrollador |

---

## Documentación Adicional

### Casos de Uso Implementados

1. **Gestión de Eventos**
    - Crear, listar, actualizar y eliminar eventos
    - Filtrar por estado, categoría, ubicación
    - Verificar disponibilidad de cupos

2. **Gestión de Asistentes**
    - Registro de asistentes
    - Inscripción a eventos
    - Check-in mediante código QR
    - Cancelación de inscripciones

3. **Procesamiento de Pagos**
    - Registro de pagos con múltiples métodos
    - Confirmación y rechazo de pagos
    - Procesamiento de reembolsos
    - Cálculo de totales

4. **Sistema de Calificaciones**
    - Calificar eventos (solo asistentes verificados)
    - Cálculo de promedios
    - Filtrado por puntuación

### Validaciones Implementadas

- Fechas futuras para eventos
- Capacidad máxima de eventos
- Emails y documentos únicos
- Solo asistentes verificados pueden calificar
- Códigos QR únicos
- Estados válidos para transiciones
- Unicidad en inscripciones

---

## Troubleshooting

### Error: "Connection refused" al iniciar

**Causa:** PostgreSQL no está corriendo o el puerto es incorrecto.

**Solución:**
1. Verificar que PostgreSQL esté corriendo
2. Verificar el puerto en pgAdmin
3. Actualizar `application.yml` con el puerto correcto

### Error: "Database does not exist"

**Solución:**
```sql
CREATE DATABASE gestion_eventos;
```

### Error: "Table doesn't exist"

**Solución:** Ejecutar `schema.sql` para crear las tablas

### Las pruebas no compilan

**Solución:** Verificar que las dependencias de test estén en el `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Licencia

Este proyecto fue desarrollado como parte del curso de Programación Orientada a Objetos de la Universidad de El Salvador.

---

## Universidad de El Salvador

**Facultad Multidisciplinaria de Occidente**   
Ingeniería en Desarrollo de Software  
Ciclo II/2025

---

## Contacto

Para consultas sobre el proyecto, contactar a cualquier miembro del equipo a través de GitHub o los canales oficiales de la asignatura.

---

**Desarrollado por el Grupo 6** - Universidad de El Salvador - 2025