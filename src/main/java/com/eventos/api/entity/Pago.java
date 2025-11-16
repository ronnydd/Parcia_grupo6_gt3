package com.eventos.api.entity;

import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private MetodoPago metodoPago;

    @NotNull(message = "El estado del pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    @Size(max = 100, message = "El número de transacción no puede exceder 100 caracteres")
    @Column(name = "numero_transaccion", unique = true, length = 100)
    private String numeroTransaccion;

    @CreationTimestamp
    @Column(name = "fecha_pago", updatable = false)
    private LocalDateTime fechaPago;

    @Size(max = 255, message = "La URL del comprobante no puede exceder 255 caracteres")
    @Column(name = "comprobante_url", length = 255)
    private String comprobanteUrl;

    // Relación con RegistroEvento (1:1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_registro", nullable = false, unique = true)
    private RegistroEvento registroEvento;
}