package com.parcao.controller;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.dto.ProdutoDTO;
import com.parcao.model.Produto;
import com.parcao.enums.MensagemEnum;
import com.parcao.service.IProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IProdutoService produtoService;

    public ProdutoController(IProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody ProdutoDTO produtoDTO) throws ProdutoJaCadastradoException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar produto", correlationId);

        Produto produto = produtoService.save(produtoDTO);

        LOGGER.info("[correlationId={}] Produto criado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<Produto> getAllProdutos() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Listando todos os produtos", correlationId);
        return produtoService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getProduto(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando produto: {}", correlationId, id);
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteProduto(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para excluir produto: {}", correlationId, id);

        produtoService.deleteById(id);

        LOGGER.info("[correlationId={}] Produto excluído com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(MensagemEnum.PRODUTO_EXCLUIDO_COM_SUCESSO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id, @Valid @RequestBody ProdutoDTO produtoDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar produto: {}", correlationId, id);

        Produto produto = produtoService.atualizarProduto(id, produtoDTO);

        LOGGER.info("[correlationId={}] Produto atualizado com sucesso", correlationId);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}/{quantidade}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> updateProdutoEstoque(@PathVariable(value = "id") Long id, @PathVariable(value = "quantidade") int quantidade) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar estoque do produto: {}", correlationId, id);

        produtoService.updateProdutoEstoque(id, quantidade);

        LOGGER.info("[correlationId={}] Estoque do produto atualizado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(MensagemEnum.PRODUTO_ATUALIZADO_COM_SUCESSO);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}