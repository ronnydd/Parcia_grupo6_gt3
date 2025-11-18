package com.eventos.api.controller;

import com.eventos.api.dto.AsistenteDTO;
import com.eventos.api.service.AsistenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asistentes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AsistenteController {

    private final AsistenteService asistenteService;

    @PostMapping
    public ResponseEntity<AsistenteDTO> crearAsistente(@Valid @RequestBody AsistenteDTO asistenteDTO) {
        AsistenteDTO nuevoAsistente = asistenteService.crearAsistente(asistenteDTO);
        return new ResponseEntity<>(nuevoAsistente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AsistenteDTO>> obtenerTodosLosAsistentes() {
        List<AsistenteDTO> asistentes = asistenteService.obtenerTodosLosAsistentes();
        return ResponseEntity.ok(asistentes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenteDTO> obtenerAsistentePorId(@PathVariable Long id) {
        AsistenteDTO asistente = asistenteService.obtenerAsistentePorId(id);
        return ResponseEntity.ok(asistente);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AsistenteDTO> obtenerAsistentePorEmail(@PathVariable String email) {
        AsistenteDTO asistente = asistenteService.obtenerAsistentePorEmail(email);
        return ResponseEntity.ok(asistente);
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<AsistenteDTO> obtenerAsistentePorDocumento(@PathVariable String documento) {
        AsistenteDTO asistente = asistenteService.obtenerAsistentePorDocumento(documento);
        return ResponseEntity.ok(asistente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenteDTO> actualizarAsistente(
            @PathVariable Long id,
            @Valid @RequestBody AsistenteDTO asistenteDTO) {
        AsistenteDTO asistenteActualizado = asistenteService.actualizarAsistente(id, asistenteDTO);
        return ResponseEntity.ok(asistenteActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<AsistenteDTO> activarDesactivarAsistente(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        AsistenteDTO asistenteActualizado = asistenteService.activarDesactivarAsistente(id, activo);
        return ResponseEntity.ok(asistenteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsistente(@PathVariable Long id) {
        asistenteService.eliminarAsistente(id);
        return ResponseEntity.noContent().build();
    }
}