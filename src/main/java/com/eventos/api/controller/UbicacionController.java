package com.eventos.api.controller;

import com.eventos.api.dto.UbicacionDTO;
import com.eventos.api.service.UbicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ubicaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @PostMapping
    public ResponseEntity<UbicacionDTO> crearUbicacion(@Valid @RequestBody UbicacionDTO ubicacionDTO) {
        UbicacionDTO nuevaUbicacion = ubicacionService.crearUbicacion(ubicacionDTO);
        return new ResponseEntity<>(nuevaUbicacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> obtenerTodasLasUbicaciones() {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerTodasLasUbicaciones();
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> obtenerUbicacionPorId(@PathVariable Long id) {
        UbicacionDTO ubicacion = ubicacionService.obtenerUbicacionPorId(id);
        return ResponseEntity.ok(ubicacion);
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesPorCiudad(@PathVariable String ciudad) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesPorCiudad(ciudad);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesPorDepartamento(@PathVariable String departamento) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesPorDepartamento(departamento);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesActivas() {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesActivas();
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesPorTipo(@PathVariable String tipo) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesPorTipo(tipo);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UbicacionDTO>> buscarUbicacionesPorNombre(@RequestParam String nombre) {
        List<UbicacionDTO> ubicaciones = ubicacionService.buscarUbicacionesPorNombre(nombre);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesPorCiudadYEstado(
            @RequestParam String ciudad,
            @RequestParam Boolean activo) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesPorCiudadYEstado(ciudad, activo);
        return ResponseEntity.ok(ubicaciones);
    }

    @GetMapping("/capacidad-minima/{capacidad}")
    public ResponseEntity<List<UbicacionDTO>> obtenerUbicacionesPorCapacidadMinima(@PathVariable Integer capacidad) {
        List<UbicacionDTO> ubicaciones = ubicacionService.obtenerUbicacionesPorCapacidadMinima(capacidad);
        return ResponseEntity.ok(ubicaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTO> actualizarUbicacion(
            @PathVariable Long id,
            @Valid @RequestBody UbicacionDTO ubicacionDTO) {
        UbicacionDTO ubicacionActualizada = ubicacionService.actualizarUbicacion(id, ubicacionDTO);
        return ResponseEntity.ok(ubicacionActualizada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UbicacionDTO> activarDesactivarUbicacion(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        UbicacionDTO ubicacionActualizada = ubicacionService.activarDesactivarUbicacion(id, activo);
        return ResponseEntity.ok(ubicacionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable Long id) {
        ubicacionService.eliminarUbicacion(id);
        return ResponseEntity.noContent().build();
    }
}
