package com.eventos.api.controller;

import com.eventos.api.dto.UsuarioDTO;
import com.eventos.api.entity.enums.RolUsuario;
import com.eventos.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorUsername(@PathVariable String username) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRol(@PathVariable RolUsuario rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosActivos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRolYEstado(
            @RequestParam RolUsuario rol,
            @RequestParam Boolean activo) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRolYEstado(rol, activo);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioDTO> activarDesactivarUsuario(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        UsuarioDTO usuarioActualizado = usuarioService.activarDesactivarUsuario(id, activo);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}/ultimo-login")
    public ResponseEntity<UsuarioDTO> actualizarUltimoLogin(@PathVariable Long id) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUltimoLogin(id);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}/cambiar-password")
    public ResponseEntity<UsuarioDTO> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {
        String passwordActual = passwords.get("passwordActual");
        String passwordNuevo = passwords.get("passwordNuevo");
        UsuarioDTO usuarioActualizado = usuarioService.cambiarPassword(id, passwordActual, passwordNuevo);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}