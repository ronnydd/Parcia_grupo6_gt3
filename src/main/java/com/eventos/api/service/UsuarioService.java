package com.eventos.api.service;

import com.eventos.api.dto.UsuarioDTO;
import com.eventos.api.entity.Usuario;
import com.eventos.api.entity.enums.RolUsuario;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Validar username único
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con el username: " + usuarioDTO.getUsername());
        }

        // Validar email único
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        Usuario usuario = convertirDTOaEntidad(usuarioDTO);
        usuario.setActivo(true);

        // En producción, lo ideal sería encriptar la contraseña con BCrypt
        // usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirEntidadADTO(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return convertirEntidadADTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con username: " + username));
        return convertirEntidadADTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return convertirEntidadADTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosPorRol(RolUsuario rol) {
        return usuarioRepository.findByRol(rol).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivo(true).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosPorRolYEstado(RolUsuario rol, Boolean activo) {
        return usuarioRepository.findByRolAndActivo(rol, activo).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar username único (si cambió)
        if (!usuarioExistente.getUsername().equals(usuarioDTO.getUsername()) &&
                usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con el username: " + usuarioDTO.getUsername());
        }

        // Validar email único (si cambió)
        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        // Actualizar campos
        usuarioExistente.setUsername(usuarioDTO.getUsername());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setRol(usuarioDTO.getRol());
        usuarioExistente.setNombreCompleto(usuarioDTO.getNombreCompleto());

        // Solo actualizar password si se proporciona uno nuevo
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            // En producción: usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            usuarioExistente.setPassword(usuarioDTO.getPassword());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return convertirEntidadADTO(usuarioActualizado);
    }

    public UsuarioDTO activarDesactivarUsuario(Long id, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(activo);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirEntidadADTO(usuarioActualizado);
    }

    public UsuarioDTO actualizarUltimoLogin(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setUltimoLogin(LocalDateTime.now());
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirEntidadADTO(usuarioActualizado);
    }

    public UsuarioDTO cambiarPassword(Long id, String passwordActual, String passwordNuevo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // En producción: validar con passwordEncoder.matches(passwordActual, usuario.getPassword())
        if (!usuario.getPassword().equals(passwordActual)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // En producción: usuario.setPassword(passwordEncoder.encode(passwordNuevo));
        usuario.setPassword(passwordNuevo);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirEntidadADTO(usuarioActualizado);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirEntidadADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());
        // NO devolver el password en el DTO por seguridad
        dto.setPassword(null);
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setUltimoLogin(usuario.getUltimoLogin());
        return dto;
    }

    private Usuario convertirDTOaEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setActivo(dto.getActivo());
        return usuario;
    }
}
