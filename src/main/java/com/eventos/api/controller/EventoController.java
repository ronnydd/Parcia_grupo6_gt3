package com.eventos.api.controller;

import com.eventos.api.dto.EventoDTO;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<EventoDTO>> obtenerTodosLosEventos() {
        List<EventoDTO> eventos = eventoService.obtenerTodosLosEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerEventoPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.obtenerEventoPorId(id);
        return ResponseEntity.ok(evento);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EventoDTO>> obtenerEventosPorEstado(@PathVariable EstadoEvento estado) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorEstado(estado);
        return ResponseEntity.ok(eventos);
    }

    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoCreado = eventoService.crearEvento(eventoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> actualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoActualizado = eventoService.actualizarEvento(id, eventoDTO);
        return ResponseEntity.ok(eventoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }
}
