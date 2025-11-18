package com.eventos.api.controller;

import com.eventos.api.dto.RegistroEventoDTO;
import com.eventos.api.entity.enums.EstadoRegistro;
import com.eventos.api.service.RegistroEventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/registros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistroEventoController {

    private final RegistroEventoService registroEventoService;

    @PostMapping
    public ResponseEntity<RegistroEventoDTO> inscribirAsistenteAEvento(
            @Valid @RequestBody RegistroEventoDTO registroDTO) {
        RegistroEventoDTO nuevoRegistro = registroEventoService.inscribirAsistenteAEvento(registroDTO);
        return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RegistroEventoDTO>> obtenerTodosLosRegistros() {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerTodosLosRegistros();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroEventoDTO> obtenerRegistroPorId(@PathVariable Long id) {
        RegistroEventoDTO registro = registroEventoService.obtenerRegistroPorId(id);
        return ResponseEntity.ok(registro);
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosPorEvento(@PathVariable Long idEvento) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosPorEvento(idEvento);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/asistente/{idAsistente}")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosPorAsistente(@PathVariable Long idAsistente) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosPorAsistente(idAsistente);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosPorEstado(@PathVariable EstadoRegistro estado) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosPorEstado(estado);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/evento/{idEvento}/estado/{estado}")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosPorEventoYEstado(
            @PathVariable Long idEvento,
            @PathVariable EstadoRegistro estado) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosPorEventoYEstado(idEvento, estado);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/asistente/{idAsistente}/estado/{estado}")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosPorAsistenteYEstado(
            @PathVariable Long idAsistente,
            @PathVariable EstadoRegistro estado) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosPorAsistenteYEstado(idAsistente, estado);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/codigo-qr/{codigoQr}")
    public ResponseEntity<RegistroEventoDTO> obtenerRegistroPorCodigoQR(@PathVariable String codigoQr) {
        RegistroEventoDTO registro = registroEventoService.obtenerRegistroPorCodigoQR(codigoQr);
        return ResponseEntity.ok(registro);
    }

    @GetMapping("/evento/{idEvento}/count")
    public ResponseEntity<Long> contarInscritosPorEvento(@PathVariable Long idEvento) {
        Long inscritos = registroEventoService.contarInscritosPorEvento(idEvento);
        return ResponseEntity.ok(inscritos);
    }

    @GetMapping("/evento/{idEvento}/activos")
    public ResponseEntity<List<RegistroEventoDTO>> obtenerRegistrosActivosPorEvento(@PathVariable Long idEvento) {
        List<RegistroEventoDTO> registros = registroEventoService.obtenerRegistrosActivosPorEvento(idEvento);
        return ResponseEntity.ok(registros);
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<RegistroEventoDTO> realizarCheckin(@PathVariable Long id) {
        RegistroEventoDTO registroActualizado = registroEventoService.realizarCheckin(id);
        return ResponseEntity.ok(registroActualizado);
    }

    @PatchMapping("/checkin-qr/{codigoQr}")
    public ResponseEntity<RegistroEventoDTO> realizarCheckinPorCodigoQR(@PathVariable String codigoQr) {
        RegistroEventoDTO registroActualizado = registroEventoService.realizarCheckinPorCodigoQR(codigoQr);
        return ResponseEntity.ok(registroActualizado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<RegistroEventoDTO> cancelarInscripcion(@PathVariable Long id) {
        RegistroEventoDTO registroActualizado = registroEventoService.cancelarInscripcion(id);
        return ResponseEntity.ok(registroActualizado);
    }

    @PatchMapping("/{id}/no-asistio")
    public ResponseEntity<RegistroEventoDTO> marcarNoAsistio(@PathVariable Long id) {
        RegistroEventoDTO registroActualizado = registroEventoService.marcarNoAsistio(id);
        return ResponseEntity.ok(registroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        registroEventoService.eliminarRegistro(id);
        return ResponseEntity.noContent().build();
    }
}