package com.eventos.api.service;

import com.eventos.api.dto.AsistenteDTO;
import com.eventos.api.entity.Asistente;
import com.eventos.api.repository.AsistenteRepository;
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
class AsistenteServiceTest {

    @Mock
    private AsistenteRepository asistenteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AsistenteService asistenteService;

    private Asistente asistente;
    private AsistenteDTO asistenteDTO;

    @BeforeEach
    void setUp() {
        asistente = new Asistente();
        asistente.setIdAsistente(1L);
        asistente.setNombre("Carlos");
        asistente.setApellido("Rodríguez");
        asistente.setEmail("carlos@email.com");
        asistente.setTelefono("7890-1234");
        asistente.setDocumentoIdentidad("12345678-9");
        asistente.setActivo(true);

        asistenteDTO = new AsistenteDTO();
        asistenteDTO.setNombre("Carlos");
        asistenteDTO.setApellido("Rodríguez");
        asistenteDTO.setEmail("carlos@email.com");
        asistenteDTO.setTelefono("7890-1234");
        asistenteDTO.setDocumentoIdentidad("12345678-9");
        asistenteDTO.setActivo(true);
    }

    @Test
    void testCrearAsistente_Exitoso() {
        when(asistenteRepository.existsByEmail(anyString())).thenReturn(false);
        when(asistenteRepository.existsByDocumentoIdentidad(anyString())).thenReturn(false);
        when(asistenteRepository.save(any(Asistente.class))).thenReturn(asistente);

        AsistenteDTO resultado = asistenteService.crearAsistente(asistenteDTO);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        verify(asistenteRepository, times(1)).save(any(Asistente.class));
    }

    @Test
    void testCrearAsistente_EmailDuplicado() {
        when(asistenteRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            asistenteService.crearAsistente(asistenteDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un asistente con el email"));
        verify(asistenteRepository, never()).save(any(Asistente.class));
    }

    @Test
    void testCrearAsistente_DocumentoDuplicado() {
        when(asistenteRepository.existsByEmail(anyString())).thenReturn(false);
        when(asistenteRepository.existsByDocumentoIdentidad(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            asistenteService.crearAsistente(asistenteDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un asistente con el documento"));
        verify(asistenteRepository, never()).save(any(Asistente.class));
    }

    @Test
    void testObtenerTodosLosAsistentes() {
        List<Asistente> asistentes = Arrays.asList(asistente);
        when(asistenteRepository.findAll()).thenReturn(asistentes);

        List<AsistenteDTO> resultado = asistenteService.obtenerTodosLosAsistentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerAsistentePorId_Encontrado() {
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));

        AsistenteDTO resultado = asistenteService.obtenerAsistentePorId(1L);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    void testObtenerAsistentePorId_NoEncontrado() {
        when(asistenteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            asistenteService.obtenerAsistentePorId(1L);
        });

        assertTrue(exception.getMessage().contains("Asistente no encontrado"));
    }

    @Test
    void testActualizarAsistente_Exitoso() {
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(asistenteRepository.existsByEmail(anyString())).thenReturn(false);
        when(asistenteRepository.existsByDocumentoIdentidad(anyString())).thenReturn(false);
        when(asistenteRepository.save(any(Asistente.class))).thenReturn(asistente);

        AsistenteDTO resultado = asistenteService.actualizarAsistente(1L, asistenteDTO);

        assertNotNull(resultado);
        verify(asistenteRepository, times(1)).save(any(Asistente.class));
    }

    @Test
    void testActivarDesactivarAsistente() {
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(asistenteRepository.save(any(Asistente.class))).thenReturn(asistente);

        AsistenteDTO resultado = asistenteService.activarDesactivarAsistente(1L, false);

        assertNotNull(resultado);
        verify(asistenteRepository, times(1)).save(any(Asistente.class));
    }

    @Test
    void testEliminarAsistente_Exitoso() {
        when(asistenteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(asistenteRepository).deleteById(1L);

        assertDoesNotThrow(() -> asistenteService.eliminarAsistente(1L));
        verify(asistenteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarAsistente_NoEncontrado() {
        when(asistenteRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            asistenteService.eliminarAsistente(1L);
        });

        assertTrue(exception.getMessage().contains("Asistente no encontrado"));
        verify(asistenteRepository, never()).deleteById(1L);
    }
}