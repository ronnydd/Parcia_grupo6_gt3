package com.eventos.api.repository;

import com.eventos.api.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    List<Ubicacion> findByCiudad(String ciudad);

    List<Ubicacion> findByDepartamento(String departamento);

    List<Ubicacion> findByActivo(Boolean activo);

    List<Ubicacion> findByTipoUbicacion(String tipoUbicacion);

    List<Ubicacion> findByNombreContainingIgnoreCase(String nombre);

    List<Ubicacion> findByCiudadAndActivo(String ciudad, Boolean activo);

    List<Ubicacion> findByCapacidadMaximaGreaterThanEqual(Integer capacidad);
}