package com.eventos.api.service;

import com.eventos.api.dto.RegistroEventoDTO;
import com.eventos.api.entity.*;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.entity.enums.EstadoRegistro;
import com.eventos.api.repository.AsistenteRepository;
import com.eventos.api.repository.EventoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroEventoServiceTest {

    @Mock
    private RegistroEventoRepository registroEventoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private AsistenteRepository asistenteRepository;

    @InjectMocks
    private RegistroEventoService registroEventoService;

    private Evento evento;
    private Asistente asistente;
    private RegistroEvento registro;
    private RegistroEventoDTO registroDTO;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setIdEvento(1L);
        evento.setNombre("Conferencia");
        evento.setFechaEvento(LocalDateTime.now().plusDays(30));
        evento.setCapacidadMaxima(100);
        evento.setPrecio(new BigDecimal("25.00"));
        evento.setEstado(EstadoEvento.ACTIVO);

        asistente = new Asistente();
        asistente.setIdAsistente(1L);
        asistente.setNombre("Carlos");
        asistente.setApellido("Rodríguez");

        registro = new RegistroEvento();
        registro.setIdRegistro(1L);
        registro.setEvento(evento);
        registro.setAsistente(asistente);
        registro.setEstado(EstadoRegistro.CONFIRMADO);
        registro.setPrecioPagado(new BigDecimal("25.00"));
        registro.setCodigoQr("QR123456");

        registroDTO = new RegistroEventoDTO();
        registroDTO.setIdEvento(1L);
        registroDTO.setIdAsistente(1L);
        registroDTO.setPrecioPagado(new BigDecimal("25.00"));
    }

    @Test
    void testInscribirAsistenteAEvento_Exitoso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.existsByEventoIdEventoAndAsistenteIdAsistente(anyLong(), anyLong()))
                .thenReturn(false);
        when(registroEventoRepository.contarRegistrosPorEventoYEstado(anyLong(), any()))
                .thenReturn(50L);
        when(registroEventoRepository.save(any(RegistroEvento.class))).thenReturn(registro);
        when(registroEventoRepository.findByCodigoQr(anyString())).thenReturn(Optional.empty());

        RegistroEventoDTO resultado = registroEventoService.inscribirAsistenteAEvento(registroDTO);

        assertNotNull(resultado);
        assertEquals(EstadoRegistro.CONFIRMADO, resultado.getEstado());
        verify(registroEventoRepository, times(1)).save(any(RegistroEvento.class));
    }

    @Test
    void testInscribirAsistenteAEvento_EventoNoActivo() {
        evento.setEstado(EstadoEvento.CANCELADO);
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.inscribirAsistenteAEvento(registroDTO);
        });

        assertTrue(exception.getMessage().contains("no está disponible para inscripciones"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testInscribirAsistenteAEvento_EventoPasado() {
        evento.setFechaEvento(LocalDateTime.now().minusDays(1));
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.inscribirAsistenteAEvento(registroDTO);
        });

        assertTrue(exception.getMessage().contains("ya ocurrió"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testInscribirAsistenteAEvento_YaInscrito() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.existsByEventoIdEventoAndAsistenteIdAsistente(anyLong(), anyLong()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.inscribirAsistenteAEvento(registroDTO);
        });

        assertTrue(exception.getMessage().contains("ya está inscrito"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testInscribirAsistenteAEvento_SinCapacidad() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.existsByEventoIdEventoAndAsistenteIdAsistente(anyLong(), anyLong()))
                .thenReturn(false);
        when(registroEventoRepository.contarRegistrosPorEventoYEstado(anyLong(), any()))
                .thenReturn(100L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.inscribirAsistenteAEvento(registroDTO);
        });

        assertTrue(exception.getMessage().contains("capacidad máxima"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testObtenerTodosLosRegistros() {
        List<RegistroEvento> registros = Arrays.asList(registro);
        when(registroEventoRepository.findAll()).thenReturn(registros);

        List<RegistroEventoDTO> resultado = registroEventoService.obtenerTodosLosRegistros();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testRealizarCheckin_Exitoso() {
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(registroEventoRepository.save(any(RegistroEvento.class))).thenReturn(registro);

        RegistroEventoDTO resultado = registroEventoService.realizarCheckin(1L);

        assertNotNull(resultado);
        verify(registroEventoRepository, times(1)).save(any(RegistroEvento.class));
    }

    @Test
    void testRealizarCheckin_NoConfirmado() {
        registro.setEstado(EstadoRegistro.CANCELADO);
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.realizarCheckin(1L);
        });

        assertTrue(exception.getMessage().contains("Solo se puede hacer check-in de registros confirmados"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testCancelarInscripcion_Exitoso() {
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(registroEventoRepository.save(any(RegistroEvento.class))).thenReturn(registro);

        RegistroEventoDTO resultado = registroEventoService.cancelarInscripcion(1L);

        assertNotNull(resultado);
        verify(registroEventoRepository, times(1)).save(any(RegistroEvento.class));
    }

    @Test
    void testCancelarInscripcion_YaCancelado() {
        registro.setEstado(EstadoRegistro.CANCELADO);
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registroEventoService.cancelarInscripcion(1L);
        });

        assertTrue(exception.getMessage().contains("ya está cancelado"));
        verify(registroEventoRepository, never()).save(any(RegistroEvento.class));
    }

    @Test
    void testMarcarNoAsistio_Exitoso() {
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(registroEventoRepository.save(any(RegistroEvento.class))).thenReturn(registro);

        RegistroEventoDTO resultado = registroEventoService.marcarNoAsistio(1L);

        assertNotNull(resultado);
        verify(registroEventoRepository, times(1)).save(any(RegistroEvento.class));
    }

    @Test
    void testEliminarRegistro_Exitoso() {
        when(registroEventoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(registroEventoRepository).deleteById(1L);

        assertDoesNotThrow(() -> registroEventoService.eliminarRegistro(1L));
        verify(registroEventoRepository, times(1)).deleteById(1L);
    }
}