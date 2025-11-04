package com.eventos.api.repository;

import com.eventos.api.entity.Asistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsistenteRepository extends JpaRepository<Asistente,Long> {

    Optional<Asistente> findByEmail(String email);

    Optional<Asistente> findByDocumentoIdentidad(String documentoIdentidad);

    boolean existsByEmail(String email);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}
