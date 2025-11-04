package com.eventos.api.service;

import com.eventos.api.dto.EventoDTO;
import com.eventos.api.entity.Evento;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {
    private final EventoRepository eventoRepository;

    public EventoDTO crearEvento(EventoDTO eventoDTO) {
        Evento evento = convertirDTOaEntidad(eventoDTO);
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

    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDTO) {
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        // Actualizar campos
        eventoExistente.setNombre(eventoDTO.getNombre());
        eventoExistente.setDescripcion(eventoDTO.getDescripcion());
        eventoExistente.setFechaEvento(eventoDTO.getFechaEvento());
        eventoExistente.setUbicacion(eventoDTO.getUbicacion());
        eventoExistente.setCapacidadMaxima(eventoDTO.getCapacidadMaxima());
        eventoExistente.setPrecio(eventoDTO.getPrecio());
        eventoExistente.setEstado(eventoDTO.getEstado());

        Evento eventoActualizado = eventoRepository.save(eventoExistente);
        return convertirEntidadADTO(eventoActualizado);
    }

    public void eliminarEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
        eventoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosPorEstado(EstadoEvento estado) {
        return eventoRepository.findByEstado(estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión
    private EventoDTO convertirEntidadADTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaEvento(evento.getFechaEvento());
        dto.setUbicacion(evento.getUbicacion());
        dto.setCapacidadMaxima(evento.getCapacidadMaxima());
        dto.setPrecio(evento.getPrecio());
        dto.setEstado(evento.getEstado());
        dto.setFechaCreacion(evento.getFechaCreacion());
        dto.setFechaActualizacion(evento.getFechaActualizacion());
        return dto;
    }

    private Evento convertirDTOaEntidad(EventoDTO dto) {
        Evento evento = new Evento();
        evento.setIdEvento(dto.getIdEvento());
        evento.setNombre(dto.getNombre());
        evento.setDescripcion(dto.getDescripcion());
        evento.setFechaEvento(dto.getFechaEvento());
        evento.setUbicacion(dto.getUbicacion());
        evento.setCapacidadMaxima(dto.getCapacidadMaxima());
        evento.setPrecio(dto.getPrecio());
        evento.setEstado(dto.getEstado());
        return evento;
    }
}
