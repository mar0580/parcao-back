package com.parcao.controller;

import com.parcao.dto.ControleDiarioEstoqueDTO;
import com.parcao.dto.FechamentoCaixaDTO;
import com.parcao.dto.FechamentoCaixaItemDTO;
import com.parcao.model.FechamentoCaixa;
import com.parcao.service.IFechamentoCaixaService;
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
class FechamentoCaixaControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IFechamentoCaixaService fechamentoCaixaService;

    @InjectMocks
    private FechamentoCaixaController fechamentoCaixaController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateFechamentoCaixaSuccessfully() {
        FechamentoCaixaDTO request = buildFechamentoCaixaDTO();
        FechamentoCaixa expected = buildFechamentoCaixa();
        when(fechamentoCaixaService.createFechamentoCaixa(request)).thenReturn(expected);

        ResponseEntity<FechamentoCaixa> response = fechamentoCaixaController.createFechamentoCaixa(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(fechamentoCaixaService, times(1)).createFechamentoCaixa(request);
        verifyNoMoreInteractions(fechamentoCaixaService);
    }

    @Test
    void shouldReturnFechamentoCaixaProdutoSuccessfully() throws Exception {
        Long idFilial = 1L;
        Long idProduto = 2L;
        String dataInicial = "2026-04-01";
        String dataFinal = "2026-04-06";
        ControleDiarioEstoqueDTO expected = buildControleDiarioEstoqueDTO();
        when(fechamentoCaixaService.buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = fechamentoCaixaController.buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(fechamentoCaixaService, times(1)).buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal);
        verifyNoMoreInteractions(fechamentoCaixaService);
    }

    @Test
    void shouldReturnFechamentoCaixaProdutoEvenWhenCorrelationIdIsMissing() throws Exception {
        MDC.remove("correlationId");
        Long idFilial = 10L;
        Long idProduto = 20L;
        String dataInicial = "2026-01-01";
        String dataFinal = "2026-01-31";
        ControleDiarioEstoqueDTO expected = new ControleDiarioEstoqueDTO();
        when(fechamentoCaixaService.buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = fechamentoCaixaController.buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(expected, response.getBody());
        verify(fechamentoCaixaService, times(1)).buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal);
        verifyNoMoreInteractions(fechamentoCaixaService);
    }

    private FechamentoCaixaDTO buildFechamentoCaixaDTO() {
        return new FechamentoCaixaDTO(
                1L,
                1L,
                2L,
                "Fechamento sem divergências",
                Set.of(new FechamentoCaixaItemDTO(5L, 10, 2, 1, 11, 0))
        );
    }

    private FechamentoCaixa buildFechamentoCaixa() {
        FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
        fechamentoCaixa.setId(1L);
        fechamentoCaixa.setIdFilial(1L);
        fechamentoCaixa.setIdUser(2L);
        fechamentoCaixa.setObservacao("Fechamento sem divergências");
        return fechamentoCaixa;
    }

    private ControleDiarioEstoqueDTO buildControleDiarioEstoqueDTO() {
        ControleDiarioEstoqueDTO dto = new ControleDiarioEstoqueDTO();
        dto.setId(2L);
        dto.setInicio(10);
        dto.setEntrada(3);
        dto.setPerda(1);
        dto.setQuantidadeFinal(8);
        dto.setSaida(4);
        dto.setObservacao("Sem divergências");
        return dto;
    }
}