package com.eventos.api.service;

import com.eventos.api.dto.UsuarioDTO;
import com.eventos.api.entity.Usuario;
import com.eventos.api.entity.enums.RolUsuario;
import com.eventos.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("admin");
        usuario.setPassword("password123");
        usuario.setEmail("admin@eventos.com");
        usuario.setRol(RolUsuario.ADMIN);
        usuario.setNombreCompleto("Administrador");
        usuario.setActivo(true);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("admin");
        usuarioDTO.setPassword("password123");
        usuarioDTO.setEmail("admin@eventos.com");
        usuarioDTO.setRol(RolUsuario.ADMIN);
        usuarioDTO.setNombreCompleto("Administrador");
        usuarioDTO.setActivo(true);
    }

    @Test
    void testCrearUsuario_Exitoso() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        assertNull(resultado.getPassword()); // Password no debe devolverse
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearUsuario_UsernameDuplicado() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario con el username"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testCrearUsuario_EmailDuplicado() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario con el email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuarioDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("admin", resultado.get(0).getUsername());
        assertNull(resultado.get(0).getPassword());
    }

    @Test
    void testObtenerUsuarioPorId_Encontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
    }

    @Test
    void testObtenerUsuarioPorId_NoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerUsuarioPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void testObtenerUsuariosPorRol() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByRol(RolUsuario.ADMIN)).thenReturn(usuarios);

        List<UsuarioDTO> resultado = usuarioService.obtenerUsuariosPorRol(RolUsuario.ADMIN);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testActualizarUsuario_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.actualizarUsuario(1L, usuarioDTO);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCambiarPassword_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.cambiarPassword(1L, "password123", "newpassword");

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCambiarPassword_PasswordIncorrecto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.cambiarPassword(1L, "wrongpassword", "newpassword");
        });

        assertTrue(exception.getMessage().contains("contraseña actual es incorrecta"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testActivarDesactivarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.activarDesactivarUsuario(1L, false);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_Exitoso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1L));
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}