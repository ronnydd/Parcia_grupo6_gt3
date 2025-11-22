package com.eventos.api.service;

import com.eventos.api.dto.CalificacionDTO;
import com.eventos.api.entity.*;
import com.eventos.api.entity.enums.EstadoRegistro;
import com.eventos.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private AsistenteRepository asistenteRepository;

    @Mock
    private RegistroEventoRepository registroEventoRepository;

    @InjectMocks
    private CalificacionService calificacionService;

    private Calificacion calificacion;
    private CalificacionDTO calificacionDTO;
    private Evento evento;
    private Asistente asistente;
    private RegistroEvento registro;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setIdEvento(1L);
        evento.setNombre("Conferencia");

        asistente = new Asistente();
        asistente.setIdAsistente(1L);
        asistente.setNombre("Carlos");
        asistente.setApellido("Rodríguez");

        registro = new RegistroEvento();
        registro.setIdRegistro(1L);
        registro.setEvento(evento);
        registro.setAsistente(asistente);
        registro.setEstado(EstadoRegistro.ASISTIO);

        calificacion = new Calificacion();
        calificacion.setIdCalificacion(1L);
        calificacion.setEvento(evento);
        calificacion.setAsistente(asistente);
        calificacion.setPuntuacion(5);
        calificacion.setComentario("Excelente evento");

        calificacionDTO = new CalificacionDTO();
        calificacionDTO.setIdEvento(1L);
        calificacionDTO.setIdAsistente(1L);
        calificacionDTO.setPuntuacion(5);
        calificacionDTO.setComentario("Excelente evento");
    }

    @Test
    void testCrearCalificacion_Exitoso() {
        List<RegistroEvento> registrosAsistio = Arrays.asList(registro);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.findByEventoIdEventoAndEstado(1L, EstadoRegistro.ASISTIO))
                .thenReturn(registrosAsistio);
        when(calificacionRepository.existsByEventoIdEventoAndAsistenteIdAsistente(anyLong(), anyLong()))
                .thenReturn(false);
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        CalificacionDTO resultado = calificacionService.crearCalificacion(calificacionDTO);

        assertNotNull(resultado);
        assertEquals(5, resultado.getPuntuacion());
        verify(calificacionRepository, times(1)).save(any(Calificacion.class));
    }

    @Test
    void testCrearCalificacion_SinAsistir() {
        List<RegistroEvento> registrosVacio = new ArrayList<>();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.findByEventoIdEventoAndEstado(1L, EstadoRegistro.ASISTIO))
                .thenReturn(registrosVacio);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calificacionService.crearCalificacion(calificacionDTO);
        });

        assertTrue(exception.getMessage().contains("debe haber asistido al evento"));
        verify(calificacionRepository, never()).save(any(Calificacion.class));
    }

    @Test
    void testCrearCalificacion_YaCalificado() {
        List<RegistroEvento> registrosAsistio = Arrays.asList(registro);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(asistenteRepository.findById(1L)).thenReturn(Optional.of(asistente));
        when(registroEventoRepository.findByEventoIdEventoAndEstado(1L, EstadoRegistro.ASISTIO))
                .thenReturn(registrosAsistio);
        when(calificacionRepository.existsByEventoIdEventoAndAsistenteIdAsistente(anyLong(), anyLong()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calificacionService.crearCalificacion(calificacionDTO);
        });

        assertTrue(exception.getMessage().contains("ya ha calificado este evento"));
        verify(calificacionRepository, never()).save(any(Calificacion.class));
    }

    @Test
    void testObtenerTodasLasCalificaciones() {
        List<Calificacion> calificaciones = Arrays.asList(calificacion);
        when(calificacionRepository.findAll()).thenReturn(calificaciones);

        List<CalificacionDTO> resultado = calificacionService.obtenerTodasLasCalificaciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getPuntuacion());
    }

    @Test
    void testObtenerCalificacionPorId_Encontrado() {
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        CalificacionDTO resultado = calificacionService.obtenerCalificacionPorId(1L);

        assertNotNull(resultado);
        assertEquals(5, resultado.getPuntuacion());
    }

    @Test
    void testCalcularPromedioCalificaciones() {
        when(eventoRepository.existsById(1L)).thenReturn(true);
        when(calificacionRepository.calcularPromedioCalificaciones(1L)).thenReturn(4.5);

        Double resultado = calificacionService.calcularPromedioCalificaciones(1L);

        assertEquals(4.5, resultado);
    }

    @Test
    void testCalcularPromedioCalificaciones_SinCalificaciones() {
        when(eventoRepository.existsById(1L)).thenReturn(true);
        when(calificacionRepository.calcularPromedioCalificaciones(1L)).thenReturn(null);

        Double resultado = calificacionService.calcularPromedioCalificaciones(1L);

        assertEquals(0.0, resultado);
    }

    @Test
    void testActualizarCalificacion_Exitoso() {
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        CalificacionDTO resultado = calificacionService.actualizarCalificacion(1L, calificacionDTO);

        assertNotNull(resultado);
        verify(calificacionRepository, times(1)).save(any(Calificacion.class));
    }

    @Test
    void testEliminarCalificacion_Exitoso() {
        when(calificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(calificacionRepository).deleteById(1L);

        assertDoesNotThrow(() -> calificacionService.eliminarCalificacion(1L));
        verify(calificacionRepository, times(1)).deleteById(1L);
    }
}