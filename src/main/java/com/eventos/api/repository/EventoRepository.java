package com.eventos.api.repository;

import com.eventos.api.entity.Evento;
import com.eventos.api.entity.enums.EstadoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByEstado(EstadoEvento estado);

    List<Evento> findByCategoriaIdCategoria(Long idCategoria);

    List<Evento> findByUbicacionIdUbicacion(Long idUbicacion);

    List<Evento> findByOrganizadorIdUsuario(Long idUsuario);

    List<Evento> findByFechaEventoAfter(LocalDateTime fecha);

    List<Evento> findByEstadoAndFechaEventoAfter(EstadoEvento estado, LocalDateTime fecha);

    List<Evento> findByNombreContainingIgnoreCase(String nombre);

    // Buscar eventos próximos (en un rango de fechas)
    @Query("SELECT e FROM Evento e WHERE e.fechaEvento BETWEEN :fechaInicio AND :fechaFin AND e.estado = :estado")
    List<Evento> findEventosProximos(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("estado") EstadoEvento estado
    );

    // Contar eventos por categoría
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.categoria.idCategoria = :idCategoria")
    Long contarEventosPorCategoria(@Param("idCategoria") Long idCategoria);
}