package com.parcao.controller;

import com.parcao.dto.PedidoDTO;
import com.parcao.dto.PedidoItemDTO;
import com.parcao.model.Pedido;
import com.parcao.service.IPedidoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IPedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreatePedidoSuccessfully() {
        PedidoDTO request = buildPedidoDTO();
        Pedido expected = buildPedido();
        when(pedidoService.save(request)).thenReturn(expected);

        ResponseEntity<Object> response = pedidoController.createPedido(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(pedidoService, times(1)).save(request);
        verifyNoMoreInteractions(pedidoService);
    }

    @Test
    void shouldCreatePedidoEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        PedidoDTO request = buildPedidoDTO();
        Pedido expected = buildPedido();
        when(pedidoService.save(request)).thenReturn(expected);

        ResponseEntity<Object> response = pedidoController.createPedido(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(expected, response.getBody());
        verify(pedidoService, times(1)).save(request);
        verifyNoMoreInteractions(pedidoService);
    }

    private PedidoDTO buildPedidoDTO() {
        return new PedidoDTO(
                1L,
                1L,
                2L,
                3L,
                4L,
                new BigDecimal("35.00"),
                new BigDecimal("20.00"),
                "PIX",
                Set.of(new PedidoItemDTO(
                        10L,
                        "Coco Gelado",
                        2,
                        new BigDecimal("17.50"),
                        new BigDecimal("35.00"),
                        new BigDecimal("20.00")
                ))
        );
    }

    private Pedido buildPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setIdFilial(1L);
        pedido.setIdCliente(2L);
        pedido.setIdUser(3L);
        pedido.setIdTaxaVenda(4L);
        pedido.setValorTotal(new BigDecimal("35.00"));
        pedido.setCustoTotal(new BigDecimal("20.00"));
        return pedido;
    }
}