package com.parcao.controller;

import com.parcao.dto.ClienteDTO;
import com.parcao.model.Cliente;
import com.parcao.enums.MensagemEnum;
import com.parcao.service.IClienteService;
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
class ClienteControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateClienteSuccessfully() {
        ClienteDTO request = buildClienteDTO();
        when(clienteService.createCliente(request)).thenReturn(buildCliente());

        ResponseEntity<Object> response = clienteController.createCliente(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.SUCESSO.getMensagem(), response.getBody())
        );
        verify(clienteService, times(1)).createCliente(request);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldReturnAllClientes() {
        List<Cliente> expected = List.of(buildCliente());
        when(clienteService.findAll()).thenReturn(expected);

        List<Cliente> response = clienteController.getAllClientes();

        assertEquals(expected, response);
        verify(clienteService, times(1)).findAll();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldReturnClientesWithPositiveBalance() {
        List<Cliente> expected = List.of(buildCliente());
        when(clienteService.findClienteBySaldoCredito()).thenReturn(expected);

        List<Cliente> response = clienteController.getAllClientesPositiveBalance();

        assertEquals(expected, response);
        verify(clienteService, times(1)).findClienteBySaldoCredito();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldReturnOkWhenClientHasPositiveBalance() {
        Long id = 1L;
        BigDecimal valorCompra = new BigDecimal("15.00");
        doNothing().when(clienteService).verificarSaldoCredito(id, valorCompra);

        ResponseEntity<Object> response = clienteController.getClientPositiveBalance(id, valorCompra);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("OK", response.getBody())
        );
        verify(clienteService, times(1)).verificarSaldoCredito(id, valorCompra);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldReturnClienteById() {
        Long id = 1L;
        Cliente expected = buildCliente();
        when(clienteService.getClienteById(id)).thenReturn(expected);

        ResponseEntity<Cliente> response = clienteController.getCliente(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(clienteService, times(1)).getClienteById(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldDeleteClienteSuccessfully() {
        Long id = 1L;
        doNothing().when(clienteService).deleteCliente(id);

        ResponseEntity<Object> response = clienteController.deleteCliente(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.EXCLUIDO_COM_SUCESSO.getMensagem(), response.getBody())
        );
        verify(clienteService, times(1)).deleteCliente(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldUpdateClienteSuccessfully() {
        Long id = 1L;
        ClienteDTO request = buildClienteDTO();
        Cliente expected = buildCliente();
        when(clienteService.updateClienteById(id, request)).thenReturn(expected);

        ResponseEntity<Cliente> response = clienteController.updateCliente(id, request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(clienteService, times(1)).updateClienteById(id, request);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldUpdateSaldoClienteSuccessfully() {
        Long id = 1L;
        BigDecimal valorCompra = new BigDecimal("25.00");
        doNothing().when(clienteService).updateSaldoClienteById(id, valorCompra);

        ResponseEntity<Object> response = clienteController.updateSaldoCliente(id, valorCompra);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.SALDO_CLIENTE_ATUALIZADO.getMensagem(), response.getBody())
        );
        verify(clienteService, times(1)).updateSaldoClienteById(id, valorCompra);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void shouldReturnAllClientesEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        when(clienteService.findAll()).thenReturn(List.of());

        List<Cliente> response = clienteController.getAllClientes();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(clienteService, times(1)).findAll();
        verifyNoMoreInteractions(clienteService);
    }

    private ClienteDTO buildClienteDTO() {
        return new ClienteDTO(1L, "Cliente Teste", "11999999999", new BigDecimal("30.00"));
    }

    private Cliente buildCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("Cliente Teste");
        cliente.setTelefone("11999999999");
        cliente.setSaldoCredito(new BigDecimal("30.00"));
        return cliente;
    }
}