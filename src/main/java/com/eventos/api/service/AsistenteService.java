package com.eventos.api.service;

import com.eventos.api.dto.AsistenteDTO;
import com.eventos.api.entity.Asistente;
import com.eventos.api.entity.Usuario;
import com.eventos.api.repository.AsistenteRepository;
import com.eventos.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AsistenteService {

    private final AsistenteRepository asistenteRepository;
    private final UsuarioRepository usuarioRepository;

    public AsistenteDTO crearAsistente(AsistenteDTO asistenteDTO) {
        // Validar email único
        if (asistenteRepository.existsByEmail(asistenteDTO.getEmail())) {
            throw new RuntimeException("Ya existe un asistente con el email: " + asistenteDTO.getEmail());
        }

        // Validar documento único
        if (asistenteRepository.existsByDocumentoIdentidad(asistenteDTO.getDocumentoIdentidad())) {
            throw new RuntimeException("Ya existe un asistente con el documento: " + asistenteDTO.getDocumentoIdentidad());
        }

        Asistente asistente = convertirDTOaEntidad(asistenteDTO);
        asistente.setActivo(true);

        // Asignar usuario si existe el ID
        if (asistenteDTO.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(asistenteDTO.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + asistenteDTO.getIdUsuario()));
            asistente.setUsuario(usuario);
        }

        Asistente asistenteGuardado = asistenteRepository.save(asistente);
        return convertirEntidadADTO(asistenteGuardado);
    }

    @Transactional(readOnly = true)
    public List<AsistenteDTO> obtenerTodosLosAsistentes() {
        return asistenteRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsistenteDTO obtenerAsistentePorId(Long id) {
        Asistente asistente = asistenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + id));
        return convertirEntidadADTO(asistente);
    }

    @Transactional(readOnly = true)
    public AsistenteDTO obtenerAsistentePorEmail(String email) {
        Asistente asistente = asistenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con email: " + email));
        return convertirEntidadADTO(asistente);
    }

    @Transactional(readOnly = true)
    public AsistenteDTO obtenerAsistentePorDocumento(String documento) {
        Asistente asistente = asistenteRepository.findByDocumentoIdentidad(documento)
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con documento: " + documento));
        return convertirEntidadADTO(asistente);
    }

    public AsistenteDTO actualizarAsistente(Long id, AsistenteDTO asistenteDTO) {
        Asistente asistenteExistente = asistenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + id));

        // Validar email único (si cambió)
        if (!asistenteExistente.getEmail().equals(asistenteDTO.getEmail()) &&
                asistenteRepository.existsByEmail(asistenteDTO.getEmail())) {
            throw new RuntimeException("Ya existe un asistente con el email: " + asistenteDTO.getEmail());
        }

        // Validar documento único (si cambió)
        if (!asistenteExistente.getDocumentoIdentidad().equals(asistenteDTO.getDocumentoIdentidad()) &&
                asistenteRepository.existsByDocumentoIdentidad(asistenteDTO.getDocumentoIdentidad())) {
            throw new RuntimeException("Ya existe un asistente con el documento: " + asistenteDTO.getDocumentoIdentidad());
        }

        // Actualizar campos
        asistenteExistente.setNombre(asistenteDTO.getNombre());
        asistenteExistente.setApellido(asistenteDTO.getApellido());
        asistenteExistente.setEmail(asistenteDTO.getEmail());
        asistenteExistente.setTelefono(asistenteDTO.getTelefono());
        asistenteExistente.setDocumentoIdentidad(asistenteDTO.getDocumentoIdentidad());

        // Actualizar usuario si cambió
        if (asistenteDTO.getIdUsuario() != null &&
                (asistenteExistente.getUsuario() == null ||
                        !asistenteExistente.getUsuario().getIdUsuario().equals(asistenteDTO.getIdUsuario()))) {
            Usuario usuario = usuarioRepository.findById(asistenteDTO.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + asistenteDTO.getIdUsuario()));
            asistenteExistente.setUsuario(usuario);
        }

        Asistente asistenteActualizado = asistenteRepository.save(asistenteExistente);
        return convertirEntidadADTO(asistenteActualizado);
    }

    public AsistenteDTO activarDesactivarAsistente(Long id, Boolean activo) {
        Asistente asistente = asistenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + id));
        asistente.setActivo(activo);
        Asistente asistenteActualizado = asistenteRepository.save(asistente);
        return convertirEntidadADTO(asistenteActualizado);
    }

    public void eliminarAsistente(Long id) {
        if (!asistenteRepository.existsById(id)) {
            throw new RuntimeException("Asistente no encontrado con ID: " + id);
        }
        asistenteRepository.deleteById(id);
    }

    private AsistenteDTO convertirEntidadADTO(Asistente asistente) {
        AsistenteDTO dto = new AsistenteDTO();
        dto.setIdAsistente(asistente.getIdAsistente());
        dto.setNombre(asistente.getNombre());
        dto.setApellido(asistente.getApellido());
        dto.setEmail(asistente.getEmail());
        dto.setTelefono(asistente.getTelefono());
        dto.setDocumentoIdentidad(asistente.getDocumentoIdentidad());
        dto.setActivo(asistente.getActivo());
        dto.setFechaRegistro(asistente.getFechaRegistro());
        dto.setFechaActualizacion(asistente.getFechaActualizacion());

        // Mapear relación con Usuario
        if (asistente.getUsuario() != null) {
            dto.setIdUsuario(asistente.getUsuario().getIdUsuario());
            dto.setUsernameUsuario(asistente.getUsuario().getUsername());
        }

        return dto;
    }

    private Asistente convertirDTOaEntidad(AsistenteDTO dto) {
        Asistente asistente = new Asistente();
        asistente.setIdAsistente(dto.getIdAsistente());
        asistente.setNombre(dto.getNombre());
        asistente.setApellido(dto.getApellido());
        asistente.setEmail(dto.getEmail());
        asistente.setTelefono(dto.getTelefono());
        asistente.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        asistente.setActivo(dto.getActivo());
        return asistente;
    }
}
