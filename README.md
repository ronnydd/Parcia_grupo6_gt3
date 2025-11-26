# API para Gestión de Eventos y Asistentes
*Sistema para administrar eventos, asistentes y registros, desarrollado con Spring Boot.*

## Información del Proyecto

**Asignatura:** Programación Orientada a Objetos  
**Grupo:** 6  
**Proyecto:** API para Gestión de Eventos y Asistentes

## Integrantes del Equipo

- **Esdras Leonel Peraza Pérez** - PP24012
- **Ana Cristina Martínez Salas** - MS24088
- **Rodrigo Alexis Mercado Calidonio** - MC24029
- **Ronny Xavier Durán Delgado** - DD23010
- **Jose Fabricio Reyes Sermeño** - RS24033

## Descripción del Proyecto

Este proyecto consiste en el desarrollo de una **API REST con Spring Boot** para la gestión integral de eventos y asistentes. La aplicación permite crear eventos en ubicaciones específicas, gestionar el registro de asistentes y controlar la capacidad máxima de cada evento.

### Lógica de Negocio Principal

- **Gestión de Eventos:** Creación, actualización, consulta y eliminación de eventos con información detallada (fecha, ubicación, capacidad, precio, etc.)
- **Gestión de Asistentes:** Registro y administración de usuarios que participarán en los eventos
- **Control de Registros:** Sistema de inscripción que respeta la capacidad máxima de cada evento
- **Sistema de Usuarios:** Diferentes roles (Administrador, Organizador, Asistente) con permisos específicos

## Arquitectura del Sistema

### Entidades Principales

- **Usuario:** Gestión de autenticación y roles del sistema
- **Evento:** Información completa de los eventos a realizar
- **Asistente:** Datos de las personas que participan en eventos
- **RegistroEvento:** Relación entre asistentes y eventos con control de capacidad

### Tecnologías Utilizadas

- **Framework:** Spring Boot
- **Lenguaje:** Java
- **Build Tool:** Gradle/Maven
- **IDE:** IntelliJ IDEA
- **Dependencias Adicionales:** Lombok (para código más limpio)
- **Testing:** Pruebas unitarias

## Documentación de Diseño

### Diagramas Incluidos

1. **Diagrama de Clases UML** - Representa las entidades principales y sus relaciones
2. **Diagrama Entidad-Relación (E-R)** - Estructura de la base de datos
3. **Casos de Uso** - Funcionalidades completas del sistema

### Funcionalidades Principales (CRUD)

#### Gestión de Eventos
- Crear eventos con validación de datos
- Consultar eventos
- Actualizar información de eventos existentes
- Eliminar eventos

#### Gestión de Asistentes
- Registrar nuevos asistentes
- Consultar información de asistentes
- Actualizar datos personales
- Eliminar asistentes del sistema

#### Gestión de Registros
- Inscribir asistentes a eventos (con control de capacidad)
- Consultar registros por evento o asistente
- Cancelar registros existentes

## Estado Actual del Proyecto

### Primera Entrega (Completada)
- [x] Diagrama de Clases UML
- [x] Diagrama Entidad-Relación
- [x] Casos de Uso detallados
- [x] Repositorio GitHub configurado
- [x] README.md inicial

### Segunda Entrega (3-9 Noviembre) - Evaluación Individual
- [x] Implementación de al menos un GET, POST, PUT o DELETE
- [x] Desarrollo de una o dos entidades conectadas
- [x] Integración de Lombok para código limpio
- [x] Implementación de DTOs (Data Transfer Objects)
- [x] Arquitectura en capas (Controller, Service, Repository)
- [x] Configuración de Spring Data JPA con base de datos
- [x] Validaciones de datos
- [x] Pruebas con Postman de las funcionalidades implementadas

### Tercera Entrega (24-30 Noviembre) - Evaluación Individual
- [x] Funcionalidad completa del sistema
- [x] Manejo correcto de todas las relaciones entre entidades
- [x] Implementación de manejo de errores
- [x] Calidad de código y buenas prácticas
- [x] Documentación final actualizada (README.md)
- [x] Pruebas unitarias con JUnit
- [x] Pruebas completas en Postman

## Configuración del Desarrollo

### Prerrequisitos
- Java 17 o superior
- IntelliJ IDEA
- Git
- Gradle/Maven

### Cómo Trabajar en el Proyecto

1. **Clonar el repositorio**
```bash
git clone https://github.com/ronnydd/Parcia_grupo6_gt3.git
```

2. **Abrir el proyecto en IntelliJ IDEA**

3. **Realizar cambios en los archivos necesarios**

4. **Guardar y hacer commit de los cambios**
```bash
git add .
git commit -m "Descripción de los cambios realizados"
```

5. **Subir cambios al repositorio**
```bash
git push origin main
```

## Estructura del Proyecto

```
Parcia_grupo6_gt3/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── docs/
│   ├── diagramas/
│   └── casos-uso/
├── README.md
└── .gitignore
```

## Contacto

Para consultas sobre el proyecto, contactar a cualquier miembro del equipo a través de GitHub o los canales oficiales de la asignatura.

---
**Universidad:** Universidad de El Salvador  
**Carrera:** Ingeniería en Desarrollo de software
**Ciclo:** II-2025
