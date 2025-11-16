package com.eventos.api.dto;

import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {

    private Long idPago;

    @NotNull(message = "El ID del registro es obligatorio")
    private Long idRegistro;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estadoPago;

    @Size(max = 100, message = "El número de transacción no puede exceder 100 caracteres")
    private String numeroTransaccion;

    private LocalDateTime fechaPago;

    @Size(max = 255, message = "La URL del comprobante no puede exceder 255 caracteres")
    private String comprobanteUrl;
}
