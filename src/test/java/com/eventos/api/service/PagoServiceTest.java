package com.eventos.api.service;

import com.eventos.api.dto.PagoDTO;
import com.eventos.api.entity.Pago;
import com.eventos.api.entity.RegistroEvento;
import com.eventos.api.entity.enums.EstadoPago;
import com.eventos.api.entity.enums.MetodoPago;
import com.eventos.api.repository.PagoRepository;
import com.eventos.api.repository.RegistroEventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private RegistroEventoRepository registroEventoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pago;
    private PagoDTO pagoDTO;
    private RegistroEvento registro;

    @BeforeEach
    void setUp() {
        registro = new RegistroEvento();
        registro.setIdRegistro(1L);

        pago = new Pago();
        pago.setIdPago(1L);
        pago.setRegistroEvento(registro);
        pago.setMonto(new BigDecimal("25.00"));
        pago.setMetodoPago(MetodoPago.TARJETA);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setNumeroTransaccion("TRX-2025-001");

        pagoDTO = new PagoDTO();
        pagoDTO.setIdRegistro(1L);
        pagoDTO.setMonto(new BigDecimal("25.00"));
        pagoDTO.setMetodoPago(MetodoPago.TARJETA);
        pagoDTO.setEstadoPago(EstadoPago.PENDIENTE);
    }

    @Test
    void testCrearPago_Exitoso() {
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(pagoRepository.existsByRegistroEventoIdRegistro(1L)).thenReturn(false);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(pagoRepository.findByNumeroTransaccion(anyString())).thenReturn(Optional.empty());

        PagoDTO resultado = pagoService.crearPago(pagoDTO);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("25.00"), resultado.getMonto());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testCrearPago_RegistroYaTienePago() {
        when(registroEventoRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(pagoRepository.existsByRegistroEventoIdRegistro(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.crearPago(pagoDTO);
        });

        assertTrue(exception.getMessage().contains("Ya existe un pago para este registro"));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void testObtenerTodosLosPagos() {
        List<Pago> pagos = Arrays.asList(pago);
        when(pagoRepository.findAll()).thenReturn(pagos);

        List<PagoDTO> resultado = pagoService.obtenerTodosLosPagos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testObtenerPagoPorId_Encontrado() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoDTO resultado = pagoService.obtenerPagoPorId(1L);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("25.00"), resultado.getMonto());
    }

    @Test
    void testConfirmarPago_Exitoso() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoDTO resultado = pagoService.confirmarPago(1L, "comprobante.pdf");

        assertNotNull(resultado);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testConfirmarPago_YaCompletado() {
        pago.setEstadoPago(EstadoPago.COMPLETADO);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.confirmarPago(1L, null);
        });

        assertTrue(exception.getMessage().contains("ya está completado"));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void testRechazarPago_Exitoso() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoDTO resultado = pagoService.rechazarPago(1L);

        assertNotNull(resultado);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testReembolsarPago_Exitoso() {
        pago.setEstadoPago(EstadoPago.COMPLETADO);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        PagoDTO resultado = pagoService.reembolsarPago(1L);

        assertNotNull(resultado);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testReembolsarPago_NoCompletado() {
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.reembolsarPago(1L);
        });

        assertTrue(exception.getMessage().contains("Solo se pueden reembolsar pagos completados"));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void testCalcularTotalPagosCompletados() {
        when(pagoRepository.sumarPagosCompletados()).thenReturn(new BigDecimal("1000.00"));

        BigDecimal resultado = pagoService.calcularTotalPagosCompletados();

        assertEquals(new BigDecimal("1000.00"), resultado);
    }
}