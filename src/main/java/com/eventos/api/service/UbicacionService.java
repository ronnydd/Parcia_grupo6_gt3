package com.eventos.api.service;

import com.eventos.api.dto.UbicacionDTO;
import com.eventos.api.entity.Ubicacion;
import com.eventos.api.repository.UbicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionDTO crearUbicacion(UbicacionDTO ubicacionDTO) {
        Ubicacion ubicacion = convertirDTOaEntidad(ubicacionDTO);
        ubicacion.setActivo(true);

        Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacion);
        return convertirEntidadADTO(ubicacionGuardada);
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerTodasLasUbicaciones() {
        return ubicacionRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UbicacionDTO obtenerUbicacionPorId(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));
        return convertirEntidadADTO(ubicacion);
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesPorCiudad(String ciudad) {
        return ubicacionRepository.findByCiudad(ciudad).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesPorDepartamento(String departamento) {
        return ubicacionRepository.findByDepartamento(departamento).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesActivas() {
        return ubicacionRepository.findByActivo(true).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesPorTipo(String tipoUbicacion) {
        return ubicacionRepository.findByTipoUbicacion(tipoUbicacion).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> buscarUbicacionesPorNombre(String nombre) {
        return ubicacionRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesPorCiudadYEstado(String ciudad, Boolean activo) {
        return ubicacionRepository.findByCiudadAndActivo(ciudad, activo).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> obtenerUbicacionesPorCapacidadMinima(Integer capacidadMinima) {
        return ubicacionRepository.findByCapacidadMaximaGreaterThanEqual(capacidadMinima).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public UbicacionDTO actualizarUbicacion(Long id, UbicacionDTO ubicacionDTO) {
        Ubicacion ubicacionExistente = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));

        // Actualizar campos
        ubicacionExistente.setNombre(ubicacionDTO.getNombre());
        ubicacionExistente.setDireccion(ubicacionDTO.getDireccion());
        ubicacionExistente.setCiudad(ubicacionDTO.getCiudad());
        ubicacionExistente.setDepartamento(ubicacionDTO.getDepartamento());
        ubicacionExistente.setCapacidadMaxima(ubicacionDTO.getCapacidadMaxima());
        ubicacionExistente.setTipoUbicacion(ubicacionDTO.getTipoUbicacion());
        ubicacionExistente.setDescripcion(ubicacionDTO.getDescripcion());

        Ubicacion ubicacionActualizada = ubicacionRepository.save(ubicacionExistente);
        return convertirEntidadADTO(ubicacionActualizada);
    }

    public UbicacionDTO activarDesactivarUbicacion(Long id, Boolean activo) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + id));
        ubicacion.setActivo(activo);
        Ubicacion ubicacionActualizada = ubicacionRepository.save(ubicacion);
        return convertirEntidadADTO(ubicacionActualizada);
    }

    public void eliminarUbicacion(Long id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new RuntimeException("Ubicación no encontrada con ID: " + id);
        }
        ubicacionRepository.deleteById(id);
    }

    private UbicacionDTO convertirEntidadADTO(Ubicacion ubicacion) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setIdUbicacion(ubicacion.getIdUbicacion());
        dto.setNombre(ubicacion.getNombre());
        dto.setDireccion(ubicacion.getDireccion());
        dto.setCiudad(ubicacion.getCiudad());
        dto.setDepartamento(ubicacion.getDepartamento());
        dto.setCapacidadMaxima(ubicacion.getCapacidadMaxima());
        dto.setTipoUbicacion(ubicacion.getTipoUbicacion());
        dto.setDescripcion(ubicacion.getDescripcion());
        dto.setActivo(ubicacion.getActivo());
        dto.setFechaCreacion(ubicacion.getFechaCreacion());
        return dto;
    }

    private Ubicacion convertirDTOaEntidad(UbicacionDTO dto) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setIdUbicacion(dto.getIdUbicacion());
        ubicacion.setNombre(dto.getNombre());
        ubicacion.setDireccion(dto.getDireccion());
        ubicacion.setCiudad(dto.getCiudad());
        ubicacion.setDepartamento(dto.getDepartamento());
        ubicacion.setCapacidadMaxima(dto.getCapacidadMaxima());
        ubicacion.setTipoUbicacion(dto.getTipoUbicacion());
        ubicacion.setDescripcion(dto.getDescripcion());
        ubicacion.setActivo(dto.getActivo());
        return ubicacion;
    }
}
