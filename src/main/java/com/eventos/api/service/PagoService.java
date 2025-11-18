package com.eventos.api.service;

import com.eventos.api.dto.PagoDTO;
import com.eventos.api.entity.Pago;
import com.eventos.api.entity.RegistroEvento;
import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import com.eventos.api.repository.PagoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RegistroEventoRepository registroEventoRepository;

    public PagoDTO crearPago(PagoDTO pagoDTO) {
        // Validar que el registro existe
        RegistroEvento registro = registroEventoRepository.findById(pagoDTO.getIdRegistro())
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + pagoDTO.getIdRegistro()));

        // Validar que no exista ya un pago para este registro
        if (pagoRepository.existsByRegistroEventoIdRegistro(pagoDTO.getIdRegistro())) {
            throw new RuntimeException("Ya existe un pago para este registro");
        }

        Pago pago = new Pago();
        pago.setRegistroEvento(registro);
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago(pagoDTO.getEstadoPago() != null ?
                pagoDTO.getEstadoPago() : EstadoPago.PENDIENTE);
        pago.setNumeroTransaccion(generarNumeroTransaccion());
        pago.setComprobanteUrl(pagoDTO.getComprobanteUrl());

        Pago pagoGuardado = pagoRepository.save(pago);
        return convertirEntidadADTO(pagoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerTodosLosPagos() {
        return pagoRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return convertirEntidadADTO(pago);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPagoPorRegistro(Long idRegistro) {
        Pago pago = pagoRepository.findByRegistroEventoIdRegistro(idRegistro)
                .orElseThrow(() -> new RuntimeException("No se encontró pago para el registro con ID: " + idRegistro));
        return convertirEntidadADTO(pago);
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPagoPorNumeroTransaccion(String numeroTransaccion) {
        Pago pago = pagoRepository.findByNumeroTransaccion(numeroTransaccion)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con número de transacción: " + numeroTransaccion));
        return convertirEntidadADTO(pago);
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosPorEstado(EstadoPago estado) {
        return pagoRepository.findByEstadoPago(estado).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosPorMetodo(MetodoPago metodo) {
        return pagoRepository.findByMetodoPago(metodo).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosPorEstadoYMetodo(EstadoPago estado, MetodoPago metodo) {
        return pagoRepository.findByEstadoPagoAndMetodoPago(estado, metodo).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pagoRepository.findByFechaPagoBetween(fechaInicio, fechaFin).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosMayoresA(BigDecimal monto) {
        return pagoRepository.findByMontoGreaterThan(monto).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPagosCompletados() {
        BigDecimal total = pagoRepository.sumarPagosCompletados();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public Long contarPagosPorEstado(EstadoPago estado) {
        return pagoRepository.contarPagosPorEstado(estado);
    }

    public PagoDTO actualizarPago(Long id, PagoDTO pagoDTO) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        // Actualizar campos
        pagoExistente.setMonto(pagoDTO.getMonto());
        pagoExistente.setMetodoPago(pagoDTO.getMetodoPago());
        pagoExistente.setEstadoPago(pagoDTO.getEstadoPago());
        pagoExistente.setComprobanteUrl(pagoDTO.getComprobanteUrl());

        Pago pagoActualizado = pagoRepository.save(pagoExistente);
        return convertirEntidadADTO(pagoActualizado);
    }

    public PagoDTO cambiarEstadoPago(Long id, EstadoPago nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        pago.setEstadoPago(nuevoEstado);
        Pago pagoActualizado = pagoRepository.save(pago);
        return convertirEntidadADTO(pagoActualizado);
    }

    public PagoDTO confirmarPago(Long id, String comprobanteUrl) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pago.getEstadoPago() == EstadoPago.COMPLETADO) {
            throw new RuntimeException("El pago ya está completado");
        }

        pago.setEstadoPago(EstadoPago.COMPLETADO);
        if (comprobanteUrl != null) {
            pago.setComprobanteUrl(comprobanteUrl);
        }

        Pago pagoActualizado = pagoRepository.save(pago);
        return convertirEntidadADTO(pagoActualizado);
    }

    public PagoDTO rechazarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pago.getEstadoPago() == EstadoPago.COMPLETADO) {
            throw new RuntimeException("No se puede rechazar un pago completado");
        }

        pago.setEstadoPago(EstadoPago.RECHAZADO);
        Pago pagoActualizado = pagoRepository.save(pago);
        return convertirEntidadADTO(pagoActualizado);
    }

    public PagoDTO reembolsarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));

        if (pago.getEstadoPago() != EstadoPago.COMPLETADO) {
            throw new RuntimeException("Solo se pueden reembolsar pagos completados");
        }

        pago.setEstadoPago(EstadoPago.REEMBOLSADO);
        Pago pagoActualizado = pagoRepository.save(pago);
        return convertirEntidadADTO(pagoActualizado);
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }

    private String generarNumeroTransaccion() {
        String prefijo = "TRX-" + LocalDateTime.now().getYear() + "-";
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String numeroTransaccion = prefijo + uuid;

        // Verificar que no exista ya este número
        if (pagoRepository.findByNumeroTransaccion(numeroTransaccion).isPresent()) {
            return generarNumeroTransaccion(); // Recursión si existe
        }

        return numeroTransaccion;
    }

    private PagoDTO convertirEntidadADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setNumeroTransaccion(pago.getNumeroTransaccion());
        dto.setFechaPago(pago.getFechaPago());
        dto.setComprobanteUrl(pago.getComprobanteUrl());

        // Mapear relación con RegistroEvento
        if (pago.getRegistroEvento() != null) {
            dto.setIdRegistro(pago.getRegistroEvento().getIdRegistro());
        }

        return dto;
    }
}
