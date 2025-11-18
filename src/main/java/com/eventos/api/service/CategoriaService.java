package com.eventos.api.service;

import com.eventos.api.dto.CategoriaDTO;
import com.eventos.api.entity.Categoria;
import com.eventos.api.repository.CategoriaRepository;
import com.eventos.api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final EventoRepository eventoRepository;

    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        // Validar nombre único
        if (categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        Categoria categoria = convertirDTOaEntidad(categoriaDTO);
        categoria.setActivo(true);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirEntidadADTO(categoriaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return convertirEntidadADTO(categoria);
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorNombre(String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con nombre: " + nombre));
        return convertirEntidadADTO(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivo(true).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarCategoriasPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long contarEventosPorCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + idCategoria);
        }
        return eventoRepository.contarEventosPorCategoria(idCategoria);
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Validar nombre único (si cambió)
        if (!categoriaExistente.getNombre().equals(categoriaDTO.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        // Actualizar campos
        categoriaExistente.setNombre(categoriaDTO.getNombre());
        categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return convertirEntidadADTO(categoriaActualizada);
    }

    public CategoriaDTO activarDesactivarCategoria(Long id, Boolean activo) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        categoria.setActivo(activo);
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return convertirEntidadADTO(categoriaActualizada);
    }

    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }

        // Validar que no tenga eventos asociados
        Long eventosAsociados = eventoRepository.contarEventosPorCategoria(id);
        if (eventosAsociados > 0) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene " +
                    eventosAsociados + " evento(s) asociado(s)");
        }

        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertirEntidadADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setActivo(categoria.getActivo());
        dto.setFechaCreacion(categoria.getFechaCreacion());
        return dto;
    }

    private Categoria convertirDTOaEntidad(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.getIdCategoria());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setActivo(dto.getActivo());
        return categoria;
    }
}
