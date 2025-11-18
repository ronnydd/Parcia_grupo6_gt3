package com.eventos.api.service;

import com.eventos.api.dto.EventoDTO;
import com.eventos.api.entity.Categoria;
import com.eventos.api.entity.Evento;
import com.eventos.api.entity.Ubicacion;
import com.eventos.api.entity.Usuario;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.repository.CategoriaRepository;
import com.eventos.api.repository.EventoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
import com.eventos.api.repository.UbicacionRepository;
import com.eventos.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroEventoRepository registroEventoRepository;

    public EventoDTO crearEvento(EventoDTO eventoDTO) {
        // Validar fecha futura
        if (eventoDTO.getFechaEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha del evento debe ser futura");
        }

        Evento evento = convertirDTOaEntidad(eventoDTO);
        evento.setEstado(EstadoEvento.ACTIVO);

        // Asignar relaciones
        asignarRelaciones(evento, eventoDTO);

        Evento eventoGuardado = eventoRepository.save(evento);
        return convertirEntidadADTO(eventoGuardado);
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerTodosLosEventos() {
        return eventoRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventoDTO obtenerEventoPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        return convertirEntidadADTO(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorEstado(EstadoEvento estado) {
        return eventoRepository.findByEstado(estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorCategoria(Long idCategoria) {
        return eventoRepository.findByCategoriaIdCategoria(idCategoria).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorUbicacion(Long idUbicacion) {
        return eventoRepository.findByUbicacionIdUbicacion(idUbicacion).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorOrganizador(Long idUsuario) {
        return eventoRepository.findByOrganizadorIdUsuario(idUsuario).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosProximos() {
        LocalDateTime ahora = LocalDateTime.now();
        return eventoRepository.findByFechaEventoAfter(ahora).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosActivosProximos() {
        LocalDateTime ahora = LocalDateTime.now();
        return eventoRepository.findByEstadoAndFechaEventoAfter(EstadoEvento.ACTIVO, ahora).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> buscarEventosPorNombre(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoEvento estado) {
        return eventoRepository.findEventosProximos(fechaInicio, fechaFin, estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long contarInscritos(Long idEvento) {
        return registroEventoRepository.contarRegistrosPorEventoYEstado(
                idEvento,
                com.eventos.api.entity.enums.EstadoRegistro.CONFIRMADO
        );
    }

    @Transactional(readOnly = true)
    public boolean verificarDisponibilidad(Long idEvento) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + idEvento));

        Long inscritos = contarInscritos(idEvento);
        return inscritos < evento.getCapacidadMaxima();
    }

    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO) {
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        // Validar que la fecha sea futura si se está actualizando
        if (eventoDTO.getFechaEvento() != null &&
                eventoDTO.getFechaEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha del evento debe ser futura");
        }

        // Actualizar campos básicos
        eventoExistente.setNombre(eventoDTO.getNombre());
        eventoExistente.setDescripcion(eventoDTO.getDescripcion());
        eventoExistente.setFechaEvento(eventoDTO.getFechaEvento());
        eventoExistente.setDuracionMinutos(eventoDTO.getDuracionMinutos());
        eventoExistente.setCapacidadMaxima(eventoDTO.getCapacidadMaxima());
        eventoExistente.setPrecio(eventoDTO.getPrecio());
        eventoExistente.setEstado(eventoDTO.getEstado());

        // Actualizar relaciones
        asignarRelaciones(eventoExistente, eventoDTO);

        Evento eventoActualizado = eventoRepository.save(eventoExistente);
        return convertirEntidadADTO(eventoActualizado);
    }

    public EventoDTO cambiarEstadoEvento(Long id, EstadoEvento nuevoEstado) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        evento.setEstado(nuevoEstado);
        Evento eventoActualizado = eventoRepository.save(evento);
        return convertirEntidadADTO(eventoActualizado);
    }

    public void eliminarEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        eventoRepository.deleteById(id);
    }

    private void asignarRelaciones(Evento evento, EventoDTO eventoDTO) {
        // Asignar categoría
        if (eventoDTO.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(eventoDTO.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + eventoDTO.getIdCategoria()));
            evento.setCategoria(categoria);
        }

        // Asignar ubicación
        if (eventoDTO.getIdUbicacion() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(eventoDTO.getIdUbicacion())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + eventoDTO.getIdUbicacion()));
            evento.setUbicacion(ubicacion);
        }

        // Asignar organizador
        if (eventoDTO.getIdUsuarioOrganizador() != null) {
            Usuario organizador = usuarioRepository.findById(eventoDTO.getIdUsuarioOrganizador())
                    .orElseThrow(() -> new RuntimeException("Usuario organizador no encontrado con ID: " + eventoDTO.getIdUsuarioOrganizador()));
            evento.setOrganizador(organizador);
        }
    }

    private EventoDTO convertirEntidadADTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaEvento(evento.getFechaEvento());
        dto.setDuracionMinutos(evento.getDuracionMinutos());
        dto.setCapacidadMaxima(evento.getCapacidadMaxima());
        dto.setPrecio(evento.getPrecio());
        dto.setEstado(evento.getEstado());
        dto.setFechaCreacion(evento.getFechaCreacion());
        dto.setFechaActualizacion(evento.getFechaActualizacion());

        // Mapear relaciones
        if (evento.getCategoria() != null) {
            dto.setIdCategoria(evento.getCategoria().getIdCategoria());
            dto.setNombreCategoria(evento.getCategoria().getNombre());
        }

        if (evento.getUbicacion() != null) {
            dto.setIdUbicacion(evento.getUbicacion().getIdUbicacion());
            dto.setNombreUbicacion(evento.getUbicacion().getNombre());
        }

        if (evento.getOrganizador() != null) {
            dto.setIdUsuarioOrganizador(evento.getOrganizador().getIdUsuario());
            dto.setNombreOrganizador(evento.getOrganizador().getNombreCompleto());
        }

        return dto;
    }

    private Evento convertirDTOaEntidad(EventoDTO dto) {
        Evento evento = new Evento();
        evento.setIdEvento(dto.getIdEvento());
        evento.setNombre(dto.getNombre());
        evento.setDescripcion(dto.getDescripcion());
        evento.setFechaEvento(dto.getFechaEvento());
        evento.setDuracionMinutos(dto.getDuracionMinutos());
        evento.setCapacidadMaxima(dto.getCapacidadMaxima());
        evento.setPrecio(dto.getPrecio());
        evento.setEstado(dto.getEstado());
        return evento;
    }
}