package com.parcao.controllers;

import com.parcao.dto.ProdutoDto;
import com.parcao.models.Produto;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduto(@Valid @RequestBody ProdutoDto produtoDto) {
        if (produtoService.existsByDescricaoProduto(produtoDto.getDescricaoProduto())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("PRODUTO_JA_CADASTRADO");
        }
        Produto produto = new Produto();
        BeanUtils.copyProperties(produtoDto, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(produto));
    }

    @GetMapping("/list")
    public List<Produto> getAllProdutos(){
        List<Produto> produto = new ArrayList<Produto>();
        produtoService.findAll().forEach(produto1 -> produto.add(produto1));
        return produto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduto(@PathVariable(value = "id") Long id){
        Optional<Produto> produtoOptional = produtoService.findById(id);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUTO_NAO_ENCONTRADO");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable (value = "id") Long id) {
        if (!produtoService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUTO_NAO_EXISTE");
        }
        produtoService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id, @Valid @RequestBody ProdutoDto produtoDto) {
        Optional<Produto> produtoOptional = produtoService.findById(id);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUTO_NAO_EXISTE");
        }
        Produto produto = new Produto();
        BeanUtils.copyProperties(produtoDto, produto);
        produto.setId(produtoOptional.get().getId());
        return  ResponseEntity.status(HttpStatus.OK).body(produtoService.save(produto));
    }

    @PutMapping("/{id}/{quantidade}")
    public ResponseEntity<Object> updateProdutoEstoque(@PathVariable(value = "id") Long id, @PathVariable(value = "quantidade") int quantidade) {
        if (produtoService.updateProdutoEstoque(id,quantidade) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERRO_AO_ATUALIZAR_ESTOQUE");
        }
        return ResponseEntity.status(HttpStatus.OK).body("PRODUTO_ATUALIZADO");
    }
}
