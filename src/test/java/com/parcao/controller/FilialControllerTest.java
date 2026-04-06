package com.parcao.controller;

import com.parcao.dto.FilialDTO;
import com.parcao.model.Filial;
import com.parcao.enums.MensagemEnum;
import com.parcao.service.IFilialService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilialControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IFilialService filialService;

    @InjectMocks
    private FilialController filialController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateFilialSuccessfully() {
        FilialDTO request = buildFilialDTO();
        Filial expected = buildFilial();
        when(filialService.createFilial(request)).thenReturn(expected);

        ResponseEntity<Filial> response = filialController.createFilial(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(filialService, times(1)).createFilial(request);
        verifyNoMoreInteractions(filialService);
    }

    @Test
    void shouldReturnAllFiliais() {
        List<Filial> expected = List.of(buildFilial());
        when(filialService.findAll()).thenReturn(expected);

        List<Filial> response = filialController.getAllFiliais();

        assertEquals(expected, response);
        verify(filialService, times(1)).findAll();
        verifyNoMoreInteractions(filialService);
    }

    @Test
    void shouldReturnFilialById() {
        Long id = 1L;
        Filial expected = buildFilial();
        when(filialService.getFilialById(id)).thenReturn(expected);

        ResponseEntity<Filial> response = filialController.getFilial(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(filialService, times(1)).getFilialById(id);
        verifyNoMoreInteractions(filialService);
    }

    @Test
    void shouldDeleteFilialSuccessfully() {
        Long id = 1L;
        doNothing().when(filialService).deleteFilial(id);

        ResponseEntity<Object> response = filialController.deleteFilial(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.SUCESSO.getMensagem(), response.getBody())
        );
        verify(filialService, times(1)).deleteFilial(id);
        verifyNoMoreInteractions(filialService);
    }

    @Test
    void shouldUpdateFilialSuccessfully() {
        Long id = 1L;
        FilialDTO request = buildFilialDTO();
        Filial expected = buildFilial();
        when(filialService.updateFilial(id, request)).thenReturn(expected);

        ResponseEntity<Filial> response = filialController.updateFilial(id, request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(filialService, times(1)).updateFilial(id, request);
        verifyNoMoreInteractions(filialService);
    }

    @Test
    void shouldReturnAllFiliaisEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        when(filialService.findAll()).thenReturn(List.of());

        List<Filial> response = filialController.getAllFiliais();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(filialService, times(1)).findAll();
        verifyNoMoreInteractions(filialService);
    }

    private FilialDTO buildFilialDTO() {
        return new FilialDTO(1L, "Centro", "Loja do centro");
    }

    private Filial buildFilial() {
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNomeLocal("Centro");
        filial.setDescricaoLocal("Loja do centro");
        return filial;
    }
}