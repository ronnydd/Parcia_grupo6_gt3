package com.eventos.api.service;

import com.eventos.api.dto.RegistroEventoDTO;
import com.eventos.api.entity.Asistente;
import com.eventos.api.entity.Evento;
import com.eventos.api.entity.RegistroEvento;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.entity.enums.EstadoRegistro;
import com.eventos.api.repository.AsistenteRepository;
import com.eventos.api.repository.EventoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroEventoService {

    private final RegistroEventoRepository registroEventoRepository;
    private final EventoRepository eventoRepository;
    private final AsistenteRepository asistenteRepository;

    public RegistroEventoDTO inscribirAsistenteAEvento(RegistroEventoDTO registroDTO) {
        // Validar que el evento existe
        Evento evento = eventoRepository.findById(registroDTO.getIdEvento())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + registroDTO.getIdEvento()));

        // Validar que el asistente existe
        Asistente asistente = asistenteRepository.findById(registroDTO.getIdAsistente())
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + registroDTO.getIdAsistente()));

        // Validar que el evento esté activo
        if (evento.getEstado() != EstadoEvento.ACTIVO) {
            throw new RuntimeException("El evento no está disponible para inscripciones");
        }

        // Validar que el evento sea futuro
        if (evento.getFechaEvento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede inscribir a un evento que ya ocurrió");
        }

        // Validar que no exista ya una inscripción
        if (registroEventoRepository.existsByEventoIdEventoAndAsistenteIdAsistente(
                registroDTO.getIdEvento(), registroDTO.getIdAsistente())) {
            throw new RuntimeException("El asistente ya está inscrito en este evento");
        }

        // Validar capacidad disponible
        Long inscritos = registroEventoRepository.contarRegistrosPorEventoYEstado(
                registroDTO.getIdEvento(), EstadoRegistro.CONFIRMADO);

        if (inscritos >= evento.getCapacidadMaxima()) {
            throw new RuntimeException("El evento ha alcanzado su capacidad máxima");
        }

        // Crear el registro
        RegistroEvento registro = new RegistroEvento();
        registro.setEvento(evento);
        registro.setAsistente(asistente);
        registro.setEstado(EstadoRegistro.CONFIRMADO);
        registro.setPrecioPagado(registroDTO.getPrecioPagado() != null ?
                registroDTO.getPrecioPagado() : evento.getPrecio());
        registro.setCodigoQr(generarCodigoQR());

        RegistroEvento registroGuardado = registroEventoRepository.save(registro);
        return convertirEntidadADTO(registroGuardado);
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerTodosLosRegistros() {
        return registroEventoRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RegistroEventoDTO obtenerRegistroPorId(Long id) {
        RegistroEvento registro = registroEventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + id));
        return convertirEntidadADTO(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosPorEvento(Long idEvento) {
        return registroEventoRepository.findByEventoIdEvento(idEvento).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosPorAsistente(Long idAsistente) {
        return registroEventoRepository.findByAsistenteIdAsistente(idAsistente).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosPorEstado(EstadoRegistro estado) {
        return registroEventoRepository.findByEstado(estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosPorEventoYEstado(Long idEvento, EstadoRegistro estado) {
        return registroEventoRepository.findByEventoIdEventoAndEstado(idEvento, estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosPorAsistenteYEstado(Long idAsistente, EstadoRegistro estado) {
        return registroEventoRepository.findByAsistenteIdAsistenteAndEstado(idAsistente, estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RegistroEventoDTO obtenerRegistroPorCodigoQR(String codigoQr) {
        RegistroEvento registro = registroEventoRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con código QR: " + codigoQr));
        return convertirEntidadADTO(registro);
    }

    @Transactional(readOnly = true)
    public Long contarInscritosPorEvento(Long idEvento) {
        return registroEventoRepository.contarRegistrosPorEventoYEstado(idEvento, EstadoRegistro.CONFIRMADO);
    }

    @Transactional(readOnly = true)
    public List<RegistroEventoDTO> obtenerRegistrosActivosPorEvento(Long idEvento) {
        return registroEventoRepository.findRegistrosActivosPorEvento(idEvento).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public RegistroEventoDTO realizarCheckin(Long idRegistro) {
        RegistroEvento registro = registroEventoRepository.findById(idRegistro)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + idRegistro));

        if (registro.getEstado() != EstadoRegistro.CONFIRMADO) {
            throw new RuntimeException("Solo se puede hacer check-in de registros confirmados");
        }

        registro.setEstado(EstadoRegistro.ASISTIO);
        registro.setFechaCheckin(LocalDateTime.now());

        RegistroEvento registroActualizado = registroEventoRepository.save(registro);
        return convertirEntidadADTO(registroActualizado);
    }

    public RegistroEventoDTO realizarCheckinPorCodigoQR(String codigoQr) {
        RegistroEvento registro = registroEventoRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con código QR: " + codigoQr));

        if (registro.getEstado() != EstadoRegistro.CONFIRMADO) {
            throw new RuntimeException("Solo se puede hacer check-in de registros confirmados");
        }

        registro.setEstado(EstadoRegistro.ASISTIO);
        registro.setFechaCheckin(LocalDateTime.now());

        RegistroEvento registroActualizado = registroEventoRepository.save(registro);
        return convertirEntidadADTO(registroActualizado);
    }

    public RegistroEventoDTO cancelarInscripcion(Long idRegistro) {
        RegistroEvento registro = registroEventoRepository.findById(idRegistro)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + idRegistro));

        if (registro.getEstado() == EstadoRegistro.CANCELADO) {
            throw new RuntimeException("El registro ya está cancelado");
        }

        if (registro.getEstado() == EstadoRegistro.ASISTIO) {
            throw new RuntimeException("No se puede cancelar un registro de un evento al que ya asistió");
        }

        registro.setEstado(EstadoRegistro.CANCELADO);
        registro.setFechaCancelacion(LocalDateTime.now());

        RegistroEvento registroActualizado = registroEventoRepository.save(registro);
        return convertirEntidadADTO(registroActualizado);
    }

    public RegistroEventoDTO marcarNoAsistio(Long idRegistro) {
        RegistroEvento registro = registroEventoRepository.findById(idRegistro)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + idRegistro));

        if (registro.getEstado() != EstadoRegistro.CONFIRMADO) {
            throw new RuntimeException("Solo se pueden marcar como 'no asistió' los registros confirmados");
        }

        registro.setEstado(EstadoRegistro.NO_ASISTIO);

        RegistroEvento registroActualizado = registroEventoRepository.save(registro);
        return convertirEntidadADTO(registroActualizado);
    }

    public void eliminarRegistro(Long id) {
        if (!registroEventoRepository.existsById(id)) {
            throw new RuntimeException("Registro no encontrado con ID: " + id);
        }
        registroEventoRepository.deleteById(id);
    }

    private String generarCodigoQR() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        // Verificar que no exista ya este código
        if (registroEventoRepository.findByCodigoQr(codigo.toString()).isPresent()) {
            return generarCodigoQR(); // Recursión si existe
        }

        return codigo.toString();
    }

    private RegistroEventoDTO convertirEntidadADTO(RegistroEvento registro) {
        RegistroEventoDTO dto = new RegistroEventoDTO();
        dto.setIdRegistro(registro.getIdRegistro());
        dto.setEstado(registro.getEstado());
        dto.setPrecioPagado(registro.getPrecioPagado());
        dto.setCodigoQr(registro.getCodigoQr());
        dto.setFechaRegistro(registro.getFechaRegistro());
        dto.setFechaCheckin(registro.getFechaCheckin());
        dto.setFechaCancelacion(registro.getFechaCancelacion());

        // Mapear relaciones
        if (registro.getEvento() != null) {
            dto.setIdEvento(registro.getEvento().getIdEvento());
            dto.setNombreEvento(registro.getEvento().getNombre());
        }

        if (registro.getAsistente() != null) {
            dto.setIdAsistente(registro.getAsistente().getIdAsistente());
            dto.setNombreAsistente(registro.getAsistente().getNombre() + " " +
                    registro.getAsistente().getApellido());
        }

        return dto;
    }
}
