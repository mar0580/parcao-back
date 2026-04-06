package com.parcao.controller;

import com.parcao.dto.EstatisticaDTO;
import com.parcao.service.IEstatisticaService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstatisticaControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IEstatisticaService estatisticaService;

    @InjectMocks
    private EstatisticaController estatisticaController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldReturnTipoPagamentoStatisticsWhenDataExists() throws Exception {
        Long idFilial = 1L;
        String dataInicial = "2026-04-01";
        String dataFinal = "2026-04-06";
        List<EstatisticaDTO> expected = List.of(buildEstatisticaDTO());
        when(estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(estatisticaService, times(1)).selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnMessageWhenTipoPagamentoStatisticsAreEmpty() throws Exception {
        Long idFilial = 1L;
        String dataInicial = "2026-04-01";
        String dataFinal = "2026-04-06";
        when(estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal)).thenReturn(List.of());

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("TIPO_DE_PAGAMENTO_NA0_ENCONTRADO", response.getBody())
        );
        verify(estatisticaService, times(1)).selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnPerdasPorMesWhenDataExists() throws Exception {
        Long idFilial = 2L;
        String dataInicial = "2026-03-01";
        String dataFinal = "2026-03-31";
        List<EstatisticaDTO> expected = List.of(buildEstatisticaDTO());
        when(estatisticaService.selectPerdasPorMes(idFilial, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = estatisticaController.buscaPerdasPorMes(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(estatisticaService, times(1)).selectPerdasPorMes(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnNotFoundWhenPerdasPorMesAreEmpty() throws Exception {
        Long idFilial = 2L;
        String dataInicial = "2026-03-01";
        String dataFinal = "2026-03-31";
        when(estatisticaService.selectPerdasPorMes(idFilial, dataInicial, dataFinal)).thenReturn(List.of());

        ResponseEntity<Object> response = estatisticaController.buscaPerdasPorMes(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("SEM_DADOS_ESTATISTICOS", response.getBody())
        );
        verify(estatisticaService, times(1)).selectPerdasPorMes(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnPerdasPorProdutoWhenDataExists() throws Exception {
        Long idFilial = 3L;
        String dataInicial = "2026-02-01";
        String dataFinal = "2026-02-28";
        List<EstatisticaDTO> expected = List.of(buildEstatisticaDTO());
        when(estatisticaService.selectPerdasPorProduto(idFilial, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = estatisticaController.buscaPerdasPorProduto(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(estatisticaService, times(1)).selectPerdasPorProduto(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnNotFoundWhenPerdasPorProdutoAreEmpty() throws Exception {
        Long idFilial = 3L;
        String dataInicial = "2026-02-01";
        String dataFinal = "2026-02-28";
        when(estatisticaService.selectPerdasPorProduto(idFilial, dataInicial, dataFinal)).thenReturn(List.of());

        ResponseEntity<Object> response = estatisticaController.buscaPerdasPorProduto(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("SEM_DADOS_ESTATISTICOS", response.getBody())
        );
        verify(estatisticaService, times(1)).selectPerdasPorProduto(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnVendasDiariasWhenDataExists() throws Exception {
        Long idFilial = 4L;
        String dataInicial = "2026-01-01";
        String dataFinal = "2026-01-31";
        List<EstatisticaDTO> expected = List.of(buildEstatisticaDTO());
        when(estatisticaService.selectTotalVendasDiaria(idFilial, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaVendasDiaria(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(estatisticaService, times(1)).selectTotalVendasDiaria(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnNotFoundWhenVendasDiariasAreEmpty() throws Exception {
        Long idFilial = 4L;
        String dataInicial = "2026-01-01";
        String dataFinal = "2026-01-31";
        when(estatisticaService.selectTotalVendasDiaria(idFilial, dataInicial, dataFinal)).thenReturn(List.of());

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaVendasDiaria(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("SEM_DADOS_ESTATISTICOS", response.getBody())
        );
        verify(estatisticaService, times(1)).selectTotalVendasDiaria(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnVendasMensaisWhenDataExists() throws Exception {
        Long idFilial = 5L;
        String dataInicial = "2025-12-01";
        String dataFinal = "2025-12-31";
        List<EstatisticaDTO> expected = List.of(buildEstatisticaDTO());
        when(estatisticaService.selectTotalVendasMensais(idFilial, dataInicial, dataFinal)).thenReturn(expected);

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaVendasMensais(idFilial, dataInicial, dataFinal);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(estatisticaService, times(1)).selectTotalVendasMensais(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    @Test
    void shouldReturnNotFoundWhenVendasMensaisAreEmptyEvenWithoutCorrelationId() throws Exception {
        MDC.remove("correlationId");
        Long idFilial = 5L;
        String dataInicial = "2025-12-01";
        String dataFinal = "2025-12-31";
        when(estatisticaService.selectTotalVendasMensais(idFilial, dataInicial, dataFinal)).thenReturn(List.of());

        ResponseEntity<Object> response = estatisticaController.buscaEstatisticaVendasMensais(idFilial, dataInicial, dataFinal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SEM_DADOS_ESTATISTICOS", response.getBody());
        verify(estatisticaService, times(1)).selectTotalVendasMensais(idFilial, dataInicial, dataFinal);
        verifyNoMoreInteractions(estatisticaService);
    }

    private EstatisticaDTO buildEstatisticaDTO() {
        return new EstatisticaDTO("PIX", 12, "ABRIL", 1, "Coco Gelado", 150, "06");
    }
}