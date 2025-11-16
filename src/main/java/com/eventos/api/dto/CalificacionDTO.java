package com.eventos.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDTO {

    private Long idCalificacion;

    @NotNull(message = "El ID del evento es obligatorio")
    private Long idEvento;

    @NotNull(message = "El ID del asistente es obligatorio")
    private Long idAsistente;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @Size(max = 1000, message = "El comentario no puede exceder 1000 caracteres")
    private String comentario;

    private LocalDateTime fechaCalificacion;

    // Información adicional para respuestas
    private String nombreEvento;
    private String nombreAsistente;
}
