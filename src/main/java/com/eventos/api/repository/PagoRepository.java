package com.eventos.api.repository;

import com.eventos.api.entity.Pago;
import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByRegistroEventoIdRegistro(Long idRegistro);

    Optional<Pago> findByNumeroTransaccion(String numeroTransaccion);

    List<Pago> findByEstadoPago(EstadoPago estadoPago);

    List<Pago> findByMetodoPago(MetodoPago metodoPago);

    List<Pago> findByEstadoPagoAndMetodoPago(EstadoPago estadoPago, MetodoPago metodoPago);

    List<Pago> findByFechaPagoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Pago> findByMontoGreaterThan(BigDecimal monto);

    // Sumar total de pagos completados
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estadoPago = 'COMPLETADO'")
    BigDecimal sumarPagosCompletados();

    // Contar pagos por estado
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.estadoPago = :estado")
    Long contarPagosPorEstado(@Param("estado") EstadoPago estado);

    // Verificar si existe pago para un registro
    boolean existsByRegistroEventoIdRegistro(Long idRegistro);
}
