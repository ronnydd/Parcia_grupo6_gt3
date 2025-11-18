package com.eventos.api.controller;

import com.eventos.api.dto.CalificacionDTO;
import com.eventos.api.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/calificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<CalificacionDTO> crearCalificacion(@Valid @RequestBody CalificacionDTO calificacionDTO) {
        CalificacionDTO nuevaCalificacion = calificacionService.crearCalificacion(calificacionDTO);
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> obtenerTodasLasCalificaciones() {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerTodasLasCalificaciones();
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> obtenerCalificacionPorId(@PathVariable Long id) {
        CalificacionDTO calificacion = calificacionService.obtenerCalificacionPorId(id);
        return ResponseEntity.ok(calificacion);
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorEvento(@PathVariable Long idEvento) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificacionesPorEvento(idEvento);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/asistente/{idAsistente}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorAsistente(@PathVariable Long idAsistente) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificacionesPorAsistente(idAsistente);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/puntuacion/{puntuacion}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesPorPuntuacion(@PathVariable Integer puntuacion) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificacionesPorPuntuacion(puntuacion);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/minimo/{puntuacion}")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesMayoresOIgualesA(@PathVariable Integer puntuacion) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificacionesMayoresOIgualesA(puntuacion);
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/evento/{idEvento}/asistente/{idAsistente}")
    public ResponseEntity<CalificacionDTO> obtenerCalificacionDeAsistenteEnEvento(
            @PathVariable Long idEvento,
            @PathVariable Long idAsistente) {
        CalificacionDTO calificacion = calificacionService.obtenerCalificacionDeAsistenteEnEvento(idEvento, idAsistente);
        return ResponseEntity.ok(calificacion);
    }

    @GetMapping("/evento/{idEvento}/promedio")
    public ResponseEntity<Double> calcularPromedioCalificaciones(@PathVariable Long idEvento) {
        Double promedio = calificacionService.calcularPromedioCalificaciones(idEvento);
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/evento/{idEvento}/count")
    public ResponseEntity<Long> contarCalificacionesPorEvento(@PathVariable Long idEvento) {
        Long cantidad = calificacionService.contarCalificacionesPorEvento(idEvento);
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/evento/{idEvento}/con-comentario")
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificacionesConComentarioPorEvento(@PathVariable Long idEvento) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificacionesConComentarioPorEvento(idEvento);
        return ResponseEntity.ok(calificaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionDTO> actualizarCalificacion(
            @PathVariable Long id,
            @Valid @RequestBody CalificacionDTO calificacionDTO) {
        CalificacionDTO calificacionActualizada = calificacionService.actualizarCalificacion(id, calificacionDTO);
        return ResponseEntity.ok(calificacionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCalificacion(@PathVariable Long id) {
        calificacionService.eliminarCalificacion(id);
        return ResponseEntity.noContent().build();
    }
}