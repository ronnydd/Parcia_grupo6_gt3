package com.eventos.api.repository;

import com.eventos.api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Categoria> findByActivo(Boolean activo);

    //(búsqueda parcial)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
