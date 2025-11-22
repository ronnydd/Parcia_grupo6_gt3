package com.eventos.api.service;

import com.eventos.api.dto.CategoriaDTO;
import com.eventos.api.entity.Categoria;
import com.eventos.api.repository.CategoriaRepository;
import com.eventos.api.repository.EventoRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Tecnología");
        categoria.setDescripcion("Eventos de tecnología");
        categoria.setActivo(true);

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNombre("Tecnología");
        categoriaDTO.setDescripcion("Eventos de tecnología");
        categoriaDTO.setActivo(true);
    }

    @Test
    void testCrearCategoria_Exitoso() {
        when(categoriaRepository.existsByNombre(anyString())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO resultado = categoriaService.crearCategoria(categoriaDTO);

        assertNotNull(resultado);
        assertEquals("Tecnología", resultado.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void testCrearCategoria_NombreDuplicado() {
        when(categoriaRepository.existsByNombre(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.crearCategoria(categoriaDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe una categoría con el nombre"));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testObtenerTodasLasCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<CategoriaDTO> resultado = categoriaService.obtenerTodasLasCategorias();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerCategoriaPorId_Encontrado() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaDTO resultado = categoriaService.obtenerCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Tecnología", resultado.getNombre());
    }

    @Test
    void testObtenerCategoriaPorId_NoEncontrado() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.obtenerCategoriaPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Categoría no encontrada"));
    }

    @Test
    void testObtenerCategoriasActivas() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findByActivo(true)).thenReturn(categorias);

        List<CategoriaDTO> resultado = categoriaService.obtenerCategoriasActivas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testActualizarCategoria_Exitoso() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.existsByNombre(anyString())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO resultado = categoriaService.actualizarCategoria(1L, categoriaDTO);

        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void testActivarDesactivarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO resultado = categoriaService.activarDesactivarCategoria(1L, false);

        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void testEliminarCategoria_Exitoso() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(eventoRepository.contarEventosPorCategoria(1L)).thenReturn(0L);
        doNothing().when(categoriaRepository).deleteById(1L);

        assertDoesNotThrow(() -> categoriaService.eliminarCategoria(1L));
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCategoria_ConEventosAsociados() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(eventoRepository.contarEventosPorCategoria(1L)).thenReturn(5L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.eliminarCategoria(1L);
        });

        assertTrue(exception.getMessage().contains("tiene"));
        assertTrue(exception.getMessage().contains("evento(s) asociado(s)"));
        verify(categoriaRepository, never()).deleteById(1L);
    }

    @Test
    void testContarEventosPorCategoria() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(eventoRepository.contarEventosPorCategoria(1L)).thenReturn(10L);

        Long resultado = categoriaService.contarEventosPorCategoria(1L);

        assertEquals(10L, resultado);
    }
}