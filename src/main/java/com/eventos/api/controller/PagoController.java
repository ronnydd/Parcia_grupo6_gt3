package com.eventos.api.controller;

import com.eventos.api.dto.PagoDTO;
import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import com.eventos.api.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoDTO> crearPago(@Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO nuevoPago = pagoService.crearPago(pagoDTO);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerTodosLosPagos() {
        List<PagoDTO> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPagoPorId(@PathVariable Long id) {
        PagoDTO pago = pagoService.obtenerPagoPorId(id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/registro/{idRegistro}")
    public ResponseEntity<PagoDTO> obtenerPagoPorRegistro(@PathVariable Long idRegistro) {
        PagoDTO pago = pagoService.obtenerPagoPorRegistro(idRegistro);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/transaccion/{numeroTransaccion}")
    public ResponseEntity<PagoDTO> obtenerPagoPorNumeroTransaccion(@PathVariable String numeroTransaccion) {
        PagoDTO pago = pagoService.obtenerPagoPorNumeroTransaccion(numeroTransaccion);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPorEstado(@PathVariable EstadoPago estado) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorEstado(estado);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/metodo/{metodo}")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPorMetodo(@PathVariable MetodoPago metodo) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorMetodo(metodo);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPorEstadoYMetodo(
            @RequestParam EstadoPago estado,
            @RequestParam MetodoPago metodo) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorEstadoYMetodo(estado, metodo);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<PagoDTO>> obtenerPagosPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/monto-mayor/{monto}")
    public ResponseEntity<List<PagoDTO>> obtenerPagosMayoresA(@PathVariable BigDecimal monto) {
        List<PagoDTO> pagos = pagoService.obtenerPagosMayoresA(monto);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/total-completados")
    public ResponseEntity<BigDecimal> calcularTotalPagosCompletados() {
        BigDecimal total = pagoService.calcularTotalPagosCompletados();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/count/estado/{estado}")
    public ResponseEntity<Long> contarPagosPorEstado(@PathVariable EstadoPago estado) {
        Long cantidad = pagoService.contarPagosPorEstado(estado);
        return ResponseEntity.ok(cantidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoActualizado = pagoService.actualizarPago(id, pagoDTO);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> cambiarEstadoPago(
            @PathVariable Long id,
            @RequestParam EstadoPago estado) {
        PagoDTO pagoActualizado = pagoService.cambiarEstadoPago(id, estado);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PagoDTO> confirmarPago(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String comprobanteUrl = body != null ? body.get("comprobanteUrl") : null;
        PagoDTO pagoActualizado = pagoService.confirmarPago(id, comprobanteUrl);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<PagoDTO> rechazarPago(@PathVariable Long id) {
        PagoDTO pagoActualizado = pagoService.rechazarPago(id);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PatchMapping("/{id}/reembolsar")
    public ResponseEntity<PagoDTO> reembolsarPago(@PathVariable Long id) {
        PagoDTO pagoActualizado = pagoService.reembolsarPago(id);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
