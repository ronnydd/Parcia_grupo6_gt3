package com.eventos.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Evento {
    private Long idEvento;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaEvento;
    private String ubicacion;
    private Integer capacidadMaxima;
    private BigDecimal precio;
    private String estado;

    public Evento() {
    }

    // Getters y Setters - TODO: FALTA

    // Métodos de negocio - TODO: FALTA

    public void crearEvento() {
        // TODO: Implementar lógica
    }

    public void actualizarEvento() {
        // TODO: Implementar lógica
    }

}
