package com.eventos.api.dto;

import com.eventos.api.entity.enums.EstadoEvento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long idEvento;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime fechaEvento;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duracionMinutos;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidadMaxima;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "El estado del evento es obligatorio")
    private EstadoEvento estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // IDs de relaciones
    private Long idUsuarioOrganizador;
    private Long idCategoria;
    private Long idUbicacion;

    // Información adicional para respuestas
    private String nombreOrganizador;
    private String nombreCategoria;
    private String nombreUbicacion;
}
