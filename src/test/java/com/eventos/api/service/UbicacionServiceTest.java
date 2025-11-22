package com.eventos.api.service;

import com.eventos.api.dto.UbicacionDTO;
import com.eventos.api.entity.Ubicacion;
import com.eventos.api.repository.UbicacionRepository;
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
class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    private Ubicacion ubicacion;
    private UbicacionDTO ubicacionDTO;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion();
        ubicacion.setIdUbicacion(1L);
        ubicacion.setNombre("Auditorio Central UES");
        ubicacion.setDireccion("Km 12.5 Carretera a Santa Ana");
        ubicacion.setCiudad("Santa Ana");
        ubicacion.setDepartamento("Santa Ana");
        ubicacion.setCapacidadMaxima(500);
        ubicacion.setTipoUbicacion("Auditorio");
        ubicacion.setDescripcion("Auditorio principal con equipamiento completo");
        ubicacion.setActivo(true);

        ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setNombre("Auditorio Central UES");
        ubicacionDTO.setDireccion("Km 12.5 Carretera a Santa Ana");
        ubicacionDTO.setCiudad("Santa Ana");
        ubicacionDTO.setDepartamento("Santa Ana");
        ubicacionDTO.setCapacidadMaxima(500);
        ubicacionDTO.setTipoUbicacion("Auditorio");
        ubicacionDTO.setDescripcion("Auditorio principal con equipamiento completo");
        ubicacionDTO.setActivo(true);
    }

    @Test
    void testCrearUbicacion_Exitoso() {
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        UbicacionDTO resultado = ubicacionService.crearUbicacion(ubicacionDTO);

        assertNotNull(resultado);
        assertEquals("Auditorio Central UES", resultado.getNombre());
        assertEquals("Santa Ana", resultado.getCiudad());
        assertEquals(500, resultado.getCapacidadMaxima());
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void testObtenerTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findAll()).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerTodasLasUbicaciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Auditorio Central UES", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerUbicacionPorId_Encontrado() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        UbicacionDTO resultado = ubicacionService.obtenerUbicacionPorId(1L);

        assertNotNull(resultado);
        assertEquals("Auditorio Central UES", resultado.getNombre());
        assertEquals(500, resultado.getCapacidadMaxima());
    }

    @Test
    void testObtenerUbicacionPorId_NoEncontrado() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ubicacionService.obtenerUbicacionPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Ubicación no encontrada"));
    }

    @Test
    void testObtenerUbicacionesPorCiudad() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByCiudad("Santa Ana")).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesPorCiudad("Santa Ana");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santa Ana", resultado.get(0).getCiudad());
    }

    @Test
    void testObtenerUbicacionesPorDepartamento() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByDepartamento("Santa Ana")).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesPorDepartamento("Santa Ana");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santa Ana", resultado.get(0).getDepartamento());
    }

    @Test
    void testObtenerUbicacionesActivas() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByActivo(true)).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesActivas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void testObtenerUbicacionesPorTipo() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByTipoUbicacion("Auditorio")).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesPorTipo("Auditorio");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Auditorio", resultado.get(0).getTipoUbicacion());
    }

    @Test
    void testBuscarUbicacionesPorNombre() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByNombreContainingIgnoreCase("Central")).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.buscarUbicacionesPorNombre("Central");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Central"));
    }

    @Test
    void testObtenerUbicacionesPorCiudadYEstado() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByCiudadAndActivo("Santa Ana", true)).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesPorCiudadYEstado("Santa Ana", true);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santa Ana", resultado.get(0).getCiudad());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void testObtenerUbicacionesPorCapacidadMinima() {
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion);
        when(ubicacionRepository.findByCapacidadMaximaGreaterThanEqual(100)).thenReturn(ubicaciones);

        List<UbicacionDTO> resultado = ubicacionService.obtenerUbicacionesPorCapacidadMinima(100);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getCapacidadMaxima() >= 100);
    }

    @Test
    void testActualizarUbicacion_Exitoso() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        ubicacionDTO.setNombre("Auditorio Central UES - Renovado");
        ubicacionDTO.setCapacidadMaxima(600);

        UbicacionDTO resultado = ubicacionService.actualizarUbicacion(1L, ubicacionDTO);

        assertNotNull(resultado);
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void testActualizarUbicacion_NoEncontrado() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ubicacionService.actualizarUbicacion(1L, ubicacionDTO);
        });

        assertTrue(exception.getMessage().contains("Ubicación no encontrada"));
        verify(ubicacionRepository, never()).save(any(Ubicacion.class));
    }

    @Test
    void testActivarDesactivarUbicacion() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        UbicacionDTO resultado = ubicacionService.activarDesactivarUbicacion(1L, false);

        assertNotNull(resultado);
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class));
    }

    @Test
    void testEliminarUbicacion_Exitoso() {
        when(ubicacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ubicacionRepository).deleteById(1L);

        assertDoesNotThrow(() -> ubicacionService.eliminarUbicacion(1L));
        verify(ubicacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUbicacion_NoEncontrado() {
        when(ubicacionRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ubicacionService.eliminarUbicacion(1L);
        });

        assertTrue(exception.getMessage().contains("Ubicación no encontrada"));
        verify(ubicacionRepository, never()).deleteById(1L);
    }
}