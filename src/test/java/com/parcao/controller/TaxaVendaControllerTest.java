package com.parcao.controller;

import com.parcao.model.dto.TaxaVendaDTO;
import com.parcao.model.entity.TaxaVenda;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.service.ITaxaVendaService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxaVendaControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private ITaxaVendaService taxaVendaService;

    @InjectMocks
    private TaxaVendaController taxaVendaController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateTaxaVendaSuccessfully() {
        TaxaVendaDTO request = buildTaxaVendaDTO();
        TaxaVenda expected = buildTaxaVenda();
        when(taxaVendaService.createTaxaVenda(request)).thenReturn(expected);

        ResponseEntity<TaxaVenda> response = taxaVendaController.createTaxaVenda(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(taxaVendaService, times(1)).createTaxaVenda(request);
        verifyNoMoreInteractions(taxaVendaService);
    }

    @Test
    void shouldReturnAllTaxaVendas() {
        List<TaxaVenda> expected = List.of(buildTaxaVenda());
        when(taxaVendaService.findAll()).thenReturn(expected);

        List<TaxaVenda> response = taxaVendaController.getAllTaxaVendas();

        assertEquals(expected, response);
        verify(taxaVendaService, times(1)).findAll();
        verifyNoMoreInteractions(taxaVendaService);
    }

    @Test
    void shouldReturnTaxaVendaById() {
        Long id = 1L;
        TaxaVenda expected = buildTaxaVenda();
        when(taxaVendaService.getTaxaVendaById(id)).thenReturn(expected);

        ResponseEntity<TaxaVenda> response = taxaVendaController.getTaxaVenda(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(taxaVendaService, times(1)).getTaxaVendaById(id);
        verifyNoMoreInteractions(taxaVendaService);
    }

    @Test
    void shouldDeleteTaxaVendaSuccessfully() {
        Long id = 1L;
        doNothing().when(taxaVendaService).deleteTaxaVenda(id);

        ResponseEntity<Object> response = taxaVendaController.deleteTaxaVenda(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.SUCESSO.getMensagem(), response.getBody())
        );
        verify(taxaVendaService, times(1)).deleteTaxaVenda(id);
        verifyNoMoreInteractions(taxaVendaService);
    }

    @Test
    void shouldUpdateTaxaVendaSuccessfully() {
        Long id = 1L;
        TaxaVendaDTO request = buildTaxaVendaDTO();
        TaxaVenda expected = buildTaxaVenda();
        when(taxaVendaService.updateTaxaVenda(id, request)).thenReturn(expected);

        ResponseEntity<TaxaVenda> response = taxaVendaController.updateTaxaVenda(id, request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(taxaVendaService, times(1)).updateTaxaVenda(id, request);
        verifyNoMoreInteractions(taxaVendaService);
    }

    @Test
    void shouldListTaxaVendasEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        List<TaxaVenda> expected = List.of();
        when(taxaVendaService.findAll()).thenReturn(expected);

        List<TaxaVenda> response = taxaVendaController.getAllTaxaVendas();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(taxaVendaService, times(1)).findAll();
        verifyNoMoreInteractions(taxaVendaService);
    }

    private TaxaVendaDTO buildTaxaVendaDTO() {
        return new TaxaVendaDTO(1L, "Cartão", new BigDecimal("2.50"), new BigDecimal("5.00"));
    }

    private TaxaVenda buildTaxaVenda() {
        TaxaVenda taxaVenda = new TaxaVenda();
        taxaVenda.setId(1L);
        taxaVenda.setNomeTaxa("Cartão");
        taxaVenda.setValorTaxa(new BigDecimal("2.50"));
        taxaVenda.setPercentualTaxa(new BigDecimal("5.00"));
        return taxaVenda;
    }
}