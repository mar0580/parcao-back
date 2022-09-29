package com.parcao.controllers;

import com.parcao.dto.ProdutoDto;
import com.parcao.models.Produto;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

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
        if(produtoService.existsByDescricaoProduto(produtoDto.getDescricaoProduto())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("PRODUTO_JA_CADASTRADO");
        }
        Produto produto = new Produto();
        produto.setDateAtualizacao(LocalDateTime.now());
        BeanUtils.copyProperties(produtoDto, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(produto));
    }
}
