package com.eventos.api.dto;

import com.eventos.api.entity.enums.EstadoRegistro;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEventoDTO {

    private Long idRegistro;

    @NotNull(message = "El ID del evento es obligatorio")
    private Long idEvento;

    @NotNull(message = "El ID del asistente es obligatorio")
    private Long idAsistente;

    @NotNull(message = "El estado del registro es obligatorio")
    private EstadoRegistro estado;

    @NotNull(message = "El precio pagado es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precioPagado;

    @Size(max = 10, message = "El código QR no puede exceder 10 caracteres")
    private String codigoQr;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCheckin;
    private LocalDateTime fechaCancelacion;

    // Información adicional para respuestas
    private String nombreEvento;
    private String nombreAsistente;
}
