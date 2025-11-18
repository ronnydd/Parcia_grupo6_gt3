package com.eventos.api.controller;

import com.eventos.api.dto.EventoDTO;
import com.eventos.api.entity.enums.EstadoEvento;
import com.eventos.api.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO nuevoEvento = eventoService.crearEvento(eventoDTO);
        return new ResponseEntity<>(nuevoEvento, HttpStatus.CREATED);
    }

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

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<EventoDTO>> obtenerEventosPorCategoria(@PathVariable Long idCategoria) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorCategoria(idCategoria);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/ubicacion/{idUbicacion}")
    public ResponseEntity<List<EventoDTO>> obtenerEventosPorUbicacion(@PathVariable Long idUbicacion) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorUbicacion(idUbicacion);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/organizador/{idUsuario}")
    public ResponseEntity<List<EventoDTO>> obtenerEventosPorOrganizador(@PathVariable Long idUsuario) {
        List<EventoDTO> eventos = eventoService.obtenerEventosPorOrganizador(idUsuario);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<EventoDTO>> obtenerEventosProximos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosProximos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/activos-proximos")
    public ResponseEntity<List<EventoDTO>> obtenerEventosActivosProximos() {
        List<EventoDTO> eventos = eventoService.obtenerEventosActivosProximos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EventoDTO>> buscarEventosPorNombre(@RequestParam String nombre) {
        List<EventoDTO> eventos = eventoService.buscarEventosPorNombre(nombre);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/rango")
    public ResponseEntity<List<EventoDTO>> obtenerEventosEnRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam EstadoEvento estado) {
        List<EventoDTO> eventos = eventoService.obtenerEventosEnRango(fechaInicio, fechaFin, estado);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}/inscritos")
    public ResponseEntity<Long> contarInscritos(@PathVariable Long id) {
        Long inscritos = eventoService.contarInscritos(id);
        return ResponseEntity.ok(inscritos);
    }

    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable Long id) {
        Boolean disponible = eventoService.verificarDisponibilidad(id);
        return ResponseEntity.ok(disponible);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> actualizarEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO eventoActualizado = eventoService.actualizarEvento(id, eventoDTO);
        return ResponseEntity.ok(eventoActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EventoDTO> cambiarEstadoEvento(
            @PathVariable Long id,
            @RequestParam EstadoEvento estado) {
        EventoDTO eventoActualizado = eventoService.cambiarEstadoEvento(id, estado);
        return ResponseEntity.ok(eventoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }
}