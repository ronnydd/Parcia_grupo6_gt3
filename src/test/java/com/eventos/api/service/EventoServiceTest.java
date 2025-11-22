package com.eventos.api.service;

import com.eventos.api.dto.EventoDTO;
import com.eventos.api.entity.*;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UbicacionRepository ubicacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RegistroEventoRepository registroEventoRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private EventoDTO eventoDTO;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Tecnología");

        ubicacion = new Ubicacion();
        ubicacion.setIdUbicacion(1L);
        ubicacion.setNombre("Auditorio Central");

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("admin");

        evento = new Evento();
        evento.setIdEvento(1L);
        evento.setNombre("Conferencia Spring Boot");
        evento.setDescripcion("Evento sobre Spring Boot");
        evento.setFechaEvento(LocalDateTime.now().plusDays(30));
        evento.setDuracionMinutos(480);
        evento.setCapacidadMaxima(200);
        evento.setPrecio(new BigDecimal("25.00"));
        evento.setEstado(EstadoEvento.ACTIVO);
        evento.setCategoria(categoria);
        evento.setUbicacion(ubicacion);
        evento.setOrganizador(usuario);

        eventoDTO = new EventoDTO();
        eventoDTO.setNombre("Conferencia Spring Boot");
        eventoDTO.setDescripcion("Evento sobre Spring Boot");
        eventoDTO.setFechaEvento(LocalDateTime.now().plusDays(30));
        eventoDTO.setDuracionMinutos(480);
        eventoDTO.setCapacidadMaxima(200);
        eventoDTO.setPrecio(new BigDecimal("25.00"));
        eventoDTO.setEstado(EstadoEvento.ACTIVO);
        eventoDTO.setIdCategoria(1L);
        eventoDTO.setIdUbicacion(1L);
        eventoDTO.setIdUsuarioOrganizador(1L);
    }

    @Test
    void testCrearEvento_Exitoso() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoDTO resultado = eventoService.crearEvento(eventoDTO);

        assertNotNull(resultado);
        assertEquals("Conferencia Spring Boot", resultado.getNombre());
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void testCrearEvento_FechaPasada() {
        eventoDTO.setFechaEvento(LocalDateTime.now().minusDays(1));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventoService.crearEvento(eventoDTO);
        });

        assertTrue(exception.getMessage().contains("fecha del evento debe ser futura"));
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    void testObtenerTodosLosEventos() {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoRepository.findAll()).thenReturn(eventos);

        List<EventoDTO> resultado = eventoService.obtenerTodosLosEventos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Conferencia Spring Boot", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerEventoPorId_Encontrado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        EventoDTO resultado = eventoService.obtenerEventoPorId(1L);

        assertNotNull(resultado);
        assertEquals("Conferencia Spring Boot", resultado.getNombre());
    }

    @Test
    void testObtenerEventoPorId_NoEncontrado() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventoService.obtenerEventoPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Evento no encontrado"));
    }

    @Test
    void testObtenerEventosPorEstado() {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoRepository.findByEstado(EstadoEvento.ACTIVO)).thenReturn(eventos);

        List<EventoDTO> resultado = eventoService.obtenerEventosPorEstado(EstadoEvento.ACTIVO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testVerificarDisponibilidad_ConEspacio() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(registroEventoRepository.contarRegistrosPorEventoYEstado(anyLong(), any()))
                .thenReturn(50L);

        boolean disponible = eventoService.verificarDisponibilidad(1L);

        assertTrue(disponible);
    }

    @Test
    void testVerificarDisponibilidad_SinEspacio() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(registroEventoRepository.contarRegistrosPorEventoYEstado(anyLong(), any()))
                .thenReturn(200L);

        boolean disponible = eventoService.verificarDisponibilidad(1L);

        assertFalse(disponible);
    }

    @Test
    void testActualizarEvento_Exitoso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoDTO resultado = eventoService.actualizarEvento(1L, eventoDTO);

        assertNotNull(resultado);
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void testCambiarEstadoEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoDTO resultado = eventoService.cambiarEstadoEvento(1L, EstadoEvento.CANCELADO);

        assertNotNull(resultado);
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void testEliminarEvento_Exitoso() {
        when(eventoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventoRepository).deleteById(1L);

        assertDoesNotThrow(() -> eventoService.eliminarEvento(1L));
        verify(eventoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarEvento_NoEncontrado() {
        when(eventoRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventoService.eliminarEvento(1L);
        });

        assertTrue(exception.getMessage().contains("Evento no encontrado"));
        verify(eventoRepository, never()).deleteById(1L);
    }
}