package com.parcao.controller;

import com.parcao.dto.AbastecimentoDTO;
import com.parcao.dto.AbastecimentoItemDTO;
import com.parcao.service.IAbastecimentoService;
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
class AbastecimentoControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IAbastecimentoService abastecimentoService;

    @InjectMocks
    private AbastecimentoController abastecimentoController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateAbastecimentoSuccessfully() {
        AbastecimentoDTO request = buildAbastecimentoDTO();
        String expected = "ABASTECIMENTO_CRIADO";
        when(abastecimentoService.createAbastecimento(request)).thenReturn(expected);

        ResponseEntity<Object> response = abastecimentoController.createAbastecimento(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(abastecimentoService, times(1)).createAbastecimento(request);
        verifyNoMoreInteractions(abastecimentoService);
    }

    @Test
    void shouldUpdateEstoqueFilialSuccessfully() {
        AbastecimentoDTO request = buildAbastecimentoDTO();
        String expected = "ESTOQUE_ATUALIZADO";
        when(abastecimentoService.updateEstoqueFilial(request)).thenReturn(expected);

        ResponseEntity<Object> response = abastecimentoController.updateEstoqueFilial(request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(abastecimentoService, times(1)).updateEstoqueFilial(request);
        verifyNoMoreInteractions(abastecimentoService);
    }

    @Test
    void shouldCreateAbastecimentoEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        AbastecimentoDTO request = buildAbastecimentoDTO();
        String expected = "ABASTECIMENTO_CRIADO";
        when(abastecimentoService.createAbastecimento(request)).thenReturn(expected);

        ResponseEntity<Object> response = abastecimentoController.createAbastecimento(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(expected, response.getBody());
        verify(abastecimentoService, times(1)).createAbastecimento(request);
        verifyNoMoreInteractions(abastecimentoService);
    }

    private AbastecimentoDTO buildAbastecimentoDTO() {
        Set<AbastecimentoItemDTO> itens = Set.of(new AbastecimentoItemDTO(1L, "Coco Gelado", 10));
        return new AbastecimentoDTO(1L, 2L, 3L, itens, itens);
    }
}