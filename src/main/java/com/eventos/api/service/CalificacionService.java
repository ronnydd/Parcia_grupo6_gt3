package com.eventos.api.service;

import com.eventos.api.dto.CalificacionDTO;
import com.eventos.api.entity.Asistente;
import com.eventos.api.entity.Calificacion;
import com.eventos.api.entity.Evento;
import com.eventos.api.entity.enums.EstadoRegistro;
import com.eventos.api.entity.RegistroEvento;
import com.eventos.api.repository.AsistenteRepository;
import com.eventos.api.repository.CalificacionRepository;
import com.eventos.api.repository.EventoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final EventoRepository eventoRepository;
    private final AsistenteRepository asistenteRepository;
    private final RegistroEventoRepository registroEventoRepository;

    public CalificacionDTO crearCalificacion(CalificacionDTO calificacionDTO) {
        // Validar que el evento existe
        Evento evento = eventoRepository.findById(calificacionDTO.getIdEvento())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + calificacionDTO.getIdEvento()));

        // Validar que el asistente existe
        Asistente asistente = asistenteRepository.findById(calificacionDTO.getIdAsistente())
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + calificacionDTO.getIdAsistente()));

        // Validar que el asistente haya asistido al evento
        List<RegistroEvento> registros = registroEventoRepository.findByEventoIdEventoAndEstado(
                calificacionDTO.getIdEvento(),
                EstadoRegistro.ASISTIO
        );

        boolean asistio = registros.stream()
                .anyMatch(registro -> registro.getAsistente().getIdAsistente().equals(calificacionDTO.getIdAsistente()));

        if (!asistio) {
            throw new RuntimeException("El asistente debe haber asistido al evento para poder calificarlo");
        }

        // Validar que no exista ya una calificación
        if (calificacionRepository.existsByEventoIdEventoAndAsistenteIdAsistente(
                calificacionDTO.getIdEvento(), calificacionDTO.getIdAsistente())) {
            throw new RuntimeException("El asistente ya ha calificado este evento");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setEvento(evento);
        calificacion.setAsistente(asistente);
        calificacion.setPuntuacion(calificacionDTO.getPuntuacion());
        calificacion.setComentario(calificacionDTO.getComentario());

        Calificacion calificacionGuardada = calificacionRepository.save(calificacion);
        return convertirEntidadADTO(calificacionGuardada);
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerTodasLasCalificaciones() {
        return calificacionRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalificacionDTO obtenerCalificacionPorId(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));
        return convertirEntidadADTO(calificacion);
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerCalificacionesPorEvento(Long idEvento) {
        return calificacionRepository.findByEventoIdEvento(idEvento).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerCalificacionesPorAsistente(Long idAsistente) {
        return calificacionRepository.findByAsistenteIdAsistente(idAsistente).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerCalificacionesPorPuntuacion(Integer puntuacion) {
        return calificacionRepository.findByPuntuacion(puntuacion).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerCalificacionesMayoresOIgualesA(Integer puntuacion) {
        return calificacionRepository.findByPuntuacionGreaterThanEqual(puntuacion).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalificacionDTO obtenerCalificacionDeAsistenteEnEvento(Long idEvento, Long idAsistente) {
        Calificacion calificacion = calificacionRepository
                .findByEventoIdEventoAndAsistenteIdAsistente(idEvento, idAsistente)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró calificación del asistente " + idAsistente +
                                " para el evento " + idEvento));
        return convertirEntidadADTO(calificacion);
    }

    @Transactional(readOnly = true)
    public Double calcularPromedioCalificaciones(Long idEvento) {
        if (!eventoRepository.existsById(idEvento)) {
            throw new RuntimeException("Evento no encontrado con ID: " + idEvento);
        }
        Double promedio = calificacionRepository.calcularPromedioCalificaciones(idEvento);
        return promedio != null ? promedio : 0.0;
    }

    @Transactional(readOnly = true)
    public Long contarCalificacionesPorEvento(Long idEvento) {
        if (!eventoRepository.existsById(idEvento)) {
            throw new RuntimeException("Evento no encontrado con ID: " + idEvento);
        }
        return calificacionRepository.contarCalificacionesPorEvento(idEvento);
    }

    @Transactional(readOnly = true)
    public List<CalificacionDTO> obtenerCalificacionesConComentarioPorEvento(Long idEvento) {
        return calificacionRepository.findCalificacionesConComentarioPorEvento(idEvento).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public CalificacionDTO actualizarCalificacion(Long id, CalificacionDTO calificacionDTO) {
        Calificacion calificacionExistente = calificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calificación no encontrada con ID: " + id));

        // Actualizar campos
        calificacionExistente.setPuntuacion(calificacionDTO.getPuntuacion());
        calificacionExistente.setComentario(calificacionDTO.getComentario());

        Calificacion calificacionActualizada = calificacionRepository.save(calificacionExistente);
        return convertirEntidadADTO(calificacionActualizada);
    }

    public void eliminarCalificacion(Long id) {
        if (!calificacionRepository.existsById(id)) {
            throw new RuntimeException("Calificación no encontrada con ID: " + id);
        }
        calificacionRepository.deleteById(id);
    }

    private CalificacionDTO convertirEntidadADTO(Calificacion calificacion) {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setIdCalificacion(calificacion.getIdCalificacion());
        dto.setPuntuacion(calificacion.getPuntuacion());
        dto.setComentario(calificacion.getComentario());
        dto.setFechaCalificacion(calificacion.getFechaCalificacion());

        // Mapear relaciones
        if (calificacion.getEvento() != null) {
            dto.setIdEvento(calificacion.getEvento().getIdEvento());
            dto.setNombreEvento(calificacion.getEvento().getNombre());
        }

        if (calificacion.getAsistente() != null) {
            dto.setIdAsistente(calificacion.getAsistente().getIdAsistente());
            dto.setNombreAsistente(calificacion.getAsistente().getNombre() + " " +
                    calificacion.getAsistente().getApellido());
        }

        return dto;
    }
}
