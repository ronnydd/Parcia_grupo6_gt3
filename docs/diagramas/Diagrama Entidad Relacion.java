@startuml
skinparam groupInheritance 2
title Diagrama de Clases UML - Sistema de Gestión de Eventos

class Evento {
  -idEvento: int
  -nombre: String
  -descripcion: String
  -fechaEvento: LocalDateTime
  -ubicacion: String
  -capacidadMaxima: int
  -precio: BigDecimal
  -estado: EstadoEvento
  --
          +crearEvento()
  +actualizarEvento()
  +cancelarEvento()
  +obtenerInformacion(): String
}

class Asistente {
  -idAsistente: int
  -nombre: String
  -apellido: String
  -email: String
  -documentoIdentidad: String
  -activo: boolean
  --
          +registrarAsistente()
  +actualizarDatos()
  +obtenerEventosInscritos(): List<Evento>
}

class RegistroEvento {
  -idRegistro: int
  -fechaRegistro: LocalDateTime
  -estado: EstadoRegistro
  -precioPagado: BigDecimal
  --
          +confirmarRegistro()
  +cancelarRegistro()
  +realizarCheckin()
}

class Usuario {
  -idUsuario: int
  -username: String
  -password: String
  -email: String
  -rol: RolUsuario
  -activo: boolean
  --
          +autenticar()
  +cambiarPassword()
  +obtenerPermisos(): List<String>
}

' Agregaciones/Composiciones
Evento *-- RegistroEvento : registros
Asistente *-- RegistroEvento : inscripciones

' Asociaciones
Usuario "1" --> "0..*" Evento : organiza
Usuario "1" --> "0..1" Asistente : representa

' Generalizaciones (Enumeraciones)
class EstadoEvento <<enumeration>> {
ACTIVO
        CANCELADO
FINALIZADO
}

class EstadoRegistro <<enumeration>> {
CONFIRMADO
        CANCELADO
ASISTIO
        NO_ASISTIO
}

class RolUsuario <<enumeration>> {
ADMIN
        ORGANIZADOR
ASISTENTE
}

        ' Dependencias
Evento ..> EstadoEvento
RegistroEvento ..> EstadoRegistro
Usuario ..> RolUsuario

@enduml