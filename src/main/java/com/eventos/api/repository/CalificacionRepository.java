package com.eventos.api.repository;

import com.eventos.api.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    List<Calificacion> findByEventoIdEvento(Long idEvento);

    List<Calificacion> findByAsistenteIdAsistente(Long idAsistente);

    List<Calificacion> findByPuntuacion(Integer puntuacion);

    List<Calificacion> findByPuntuacionGreaterThanEqual(Integer puntuacion);

    // Verificar si existe calificación de asistente para evento
    boolean existsByEventoIdEventoAndAsistenteIdAsistente(Long idEvento, Long idAsistente);

    Optional<Calificacion> findByEventoIdEventoAndAsistenteIdAsistente(Long idEvento, Long idAsistente);

    // Calcular promedio de calificaciones de un evento
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.evento.idEvento = :idEvento")
    Double calcularPromedioCalificaciones(@Param("idEvento") Long idEvento);

    // Contar calificaciones por evento
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.evento.idEvento = :idEvento")
    Long contarCalificacionesPorEvento(@Param("idEvento") Long idEvento);

    // Obtener calificaciones con comentario
    @Query("SELECT c FROM Calificacion c WHERE c.evento.idEvento = :idEvento AND c.comentario IS NOT NULL")
    List<Calificacion> findCalificacionesConComentarioPorEvento(@Param("idEvento") Long idEvento);
}