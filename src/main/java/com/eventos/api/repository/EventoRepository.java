package com.eventos.api.repository;

import com.eventos.api.entity.Evento;
import com.eventos.api.entity.enums.EstadoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByEstado(EstadoEvento estado);
    List<Evento> findByUbicacionContainingIgnoreCase(String ubicacion);
}
