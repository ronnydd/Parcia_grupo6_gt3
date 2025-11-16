package com.eventos.api.repository;

import com.eventos.api.entity.RegistroEvento;
import com.eventos.api.entity.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroEventoRepository extends JpaRepository<RegistroEvento, Long> {

    List<RegistroEvento> findByEventoIdEvento(Long idEvento);

    List<RegistroEvento> findByAsistenteIdAsistente(Long idAsistente);

    List<RegistroEvento> findByEstado(EstadoRegistro estado);

    List<RegistroEvento> findByEventoIdEventoAndEstado(Long idEvento, EstadoRegistro estado);

    List<RegistroEvento> findByAsistenteIdAsistenteAndEstado(Long idAsistente, EstadoRegistro estado);

    boolean existsByEventoIdEventoAndAsistenteIdAsistente(Long idEvento, Long idAsistente);

    Optional<RegistroEvento> findByCodigoQr(String codigoQr);

    // Contar registros por evento
    @Query("SELECT COUNT(r) FROM RegistroEvento r WHERE r.evento.idEvento = :idEvento AND r.estado = :estado")
    Long contarRegistrosPorEventoYEstado(@Param("idEvento") Long idEvento, @Param("estado") EstadoRegistro estado);

    // Obtener registros de un evento con estado confirmado
    @Query("SELECT r FROM RegistroEvento r WHERE r.evento.idEvento = :idEvento AND r.estado IN ('CONFIRMADO', 'ASISTIO')")
    List<RegistroEvento> findRegistrosActivosPorEvento(@Param("idEvento") Long idEvento);
}
