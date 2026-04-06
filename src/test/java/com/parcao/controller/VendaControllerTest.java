package com.parcao.controller;

import com.parcao.model.dto.ControleDiarioValoresDTO;
import com.parcao.service.IVendaService;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IVendaService vendaService;

    @InjectMocks
    private VendaController vendaController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldReturnDailySalesSummarySuccessfully() throws Exception {
        Long idFilial = 1L;
        Long idProduto = 2L;
        String dataInicial = "2026-04-01";
        String dataFinal = "2026-04-06";
        ControleDiarioValoresDTO expected = buildControleDiarioValoresDTO();
        when(vendaService.buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = vendaController.buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(vendaService, times(1)).buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);
        verifyNoMoreInteractions(vendaService);
    }

    @Test
    void shouldReturnDailySalesSummaryEvenWhenCorrelationIdIsMissing() throws Exception {
        MDC.remove("correlationId");
        Long idFilial = 3L;
        Long idProduto = 4L;
        String dataInicial = "2026-03-01";
        String dataFinal = "2026-03-31";
        ControleDiarioValoresDTO expected = new ControleDiarioValoresDTO();
        when(vendaService.buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = vendaController.buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(expected, response.getBody());
        verify(vendaService, times(1)).buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);
        verifyNoMoreInteractions(vendaService);
    }

    private ControleDiarioValoresDTO buildControleDiarioValoresDTO() {
        return new ControleDiarioValoresDTO(
                2L,
                10,
                5,
                1,
                14,
                "Sem divergências",
                4,
                new BigDecimal("8.50"),
                new BigDecimal("34.00"),
                new BigDecimal("12.00"),
                new BigDecimal("48.00"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00"),
                new BigDecimal("90.00")
        );
    }
}