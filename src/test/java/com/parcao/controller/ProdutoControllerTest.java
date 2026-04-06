package com.parcao.controller;

import com.parcao.dto.ProdutoDTO;
import com.parcao.model.Produto;
import com.parcao.enums.MensagemEnum;
import com.parcao.service.IProdutoService;
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
class ProdutoControllerTest {

    private static final String CORRELATION_ID = "test-correlation-id";

    @Mock
    private IProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    void setUp() {
        MDC.put("correlationId", CORRELATION_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateProdutoSuccessfully() throws Exception {
        ProdutoDTO request = buildProdutoDTO();
        Produto expected = buildProduto();
        when(produtoService.save(request)).thenReturn(expected);

        ResponseEntity<Produto> response = produtoController.createProduto(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(produtoService, times(1)).save(request);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldReturnAllProdutos() {
        List<Produto> expected = List.of(buildProduto());
        when(produtoService.findAll()).thenReturn(expected);

        List<Produto> response = produtoController.getAllProdutos();

        assertEquals(expected, response);
        verify(produtoService, times(1)).findAll();
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldReturnProdutoById() {
        Long id = 1L;
        Produto expected = buildProduto();
        when(produtoService.findById(id)).thenReturn(expected);

        ResponseEntity<Object> response = produtoController.getProduto(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(produtoService, times(1)).findById(id);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldDeleteProdutoSuccessfully() {
        Long id = 1L;
        doNothing().when(produtoService).deleteById(id);

        ResponseEntity<Object> response = produtoController.deleteProduto(id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.PRODUTO_EXCLUIDO_COM_SUCESSO, response.getBody())
        );
        verify(produtoService, times(1)).deleteById(id);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldUpdateProdutoSuccessfully() {
        Long id = 1L;
        ProdutoDTO request = buildProdutoDTO();
        Produto expected = buildProduto();
        when(produtoService.atualizarProduto(id, request)).thenReturn(expected);

        ResponseEntity<Object> response = produtoController.updateProduto(id, request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertSame(expected, response.getBody())
        );
        verify(produtoService, times(1)).atualizarProduto(id, request);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldUpdateProdutoEstoqueSuccessfully() {
        Long id = 1L;
        int quantidade = 5;
        when(produtoService.updateProdutoEstoque(id, quantidade)).thenReturn(10);

        ResponseEntity<Object> response = produtoController.updateProdutoEstoque(id, quantidade);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MensagemEnum.PRODUTO_ATUALIZADO_COM_SUCESSO, response.getBody())
        );
        verify(produtoService, times(1)).updateProdutoEstoque(id, quantidade);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void shouldListProdutosEvenWhenCorrelationIdIsMissing() {
        MDC.remove("correlationId");
        when(produtoService.findAll()).thenReturn(List.of());

        List<Produto> response = produtoController.getAllProdutos();

        assertNotNull(response);
        assertEquals(0, response.size());
        verify(produtoService, times(1)).findAll();
        verifyNoMoreInteractions(produtoService);
    }

    private ProdutoDTO buildProdutoDTO() {
        return new ProdutoDTO(1L, "Coco Gelado", 15, new BigDecimal("12.50"), new BigDecimal("8.30"));
    }

    private Produto buildProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setDescricaoProduto("Coco Gelado");
        produto.setQuantidade(15);
        produto.setValorUnitario(new BigDecimal("12.50"));
        produto.setValorCustoUnitario(new BigDecimal("8.30"));
        return produto;
    }
}