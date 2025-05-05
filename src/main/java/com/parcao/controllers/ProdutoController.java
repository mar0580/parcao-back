package com.parcao.controllers;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.model.dto.ProdutoDTO;
import com.parcao.model.entity.Produto;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.services.IProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    final IProdutoService produtoService;

    public ProdutoController(IProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody ProdutoDTO ProdutoDTO) throws ProdutoJaCadastradoException {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(ProdutoDTO));
    }

    @GetMapping("/list")
    public List<Produto> getAllProdutos(){
        return new ArrayList<>(produtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduto(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable (value = "id") Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(MensagemEnum.PRODUTO_EXCLUIDO_COM_SUCESSO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id, @Valid @RequestBody ProdutoDTO ProdutoDTO) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, ProdutoDTO));
    }

    @PutMapping("/{id}/{quantidade}")
    public ResponseEntity<Object> updateProdutoEstoque(@PathVariable(value = "id") Long id, @PathVariable(value = "quantidade") int quantidade) {
        produtoService.updateProdutoEstoque(id,quantidade);
        return ResponseEntity.status(HttpStatus.OK).body(MensagemEnum.PRODUTO_ATUALIZADO_COM_SUCESSO);
    }
}