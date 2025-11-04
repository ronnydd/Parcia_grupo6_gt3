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
public class AsistenteController {

    private final AsistenteService asistenteService;

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

    @PostMapping
    public ResponseEntity<AsistenteDTO> crearAsistente(@Valid @RequestBody AsistenteDTO asistenteDTO) {
        AsistenteDTO asistenteCreado = asistenteService.crearAsistente(asistenteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenteCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenteDTO> actualizarAsistente(
            @PathVariable Long id,
            @Valid @RequestBody AsistenteDTO asistenteDTO) {
        AsistenteDTO asistenteActualizado = asistenteService.actualizarAsistente(id, asistenteDTO);
        return ResponseEntity.ok(asistenteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsistente(@PathVariable Long id) {
        asistenteService.eliminarAsistente(id);
        return ResponseEntity.noContent().build();
    }
}
