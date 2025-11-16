package com.eventos.api.entity;

import com.eventos.api.entity.enums.EstadoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_evento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_evento", "id_asistente"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long idRegistro;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @NotNull(message = "El estado del registro es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoRegistro estado = EstadoRegistro.CONFIRMADO;

    @NotNull(message = "El precio pagado es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(name = "precio_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPagado;

    @Size(max = 10, message = "El código QR no puede exceder 10 caracteres")
    @Column(name = "codigo_qr", unique = true, length = 10)
    private String codigoQr;

    @Column(name = "fecha_checkin")
    private LocalDateTime fechaCheckin;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asistente", nullable = false)
    private Asistente asistente;

    @OneToOne(mappedBy = "registroEvento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pago pago;
}