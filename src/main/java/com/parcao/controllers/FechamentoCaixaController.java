package com.parcao.controllers;

import com.parcao.dto.FechamentoCaixaDto;
import com.parcao.dto.FechamentoCaixaItemDto;
import com.parcao.models.FechamentoCaixa;
import com.parcao.models.FechamentoCaixaItem;
import com.parcao.security.services.FechamentoCaixaService;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fechamentocaixa")
public class FechamentoCaixaController {

    final FechamentoCaixaService fechamentoCaixaService;
    final ProdutoService produtoService;

    public FechamentoCaixaController(FechamentoCaixaService fechamentoCaixaService, ProdutoService produtoService) {
        this.fechamentoCaixaService = fechamentoCaixaService;
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFechamentoCaixa(@Valid @RequestBody FechamentoCaixaDto fechamentoCaixaDto) {
        Set<FechamentoCaixaItemDto> produtoItemFechamentoCaixaDto = fechamentoCaixaDto.getProdutos();
        Set<FechamentoCaixaItem> produtos = new HashSet<>();

        produtoItemFechamentoCaixaDto.forEach(produto -> produtos.add(produtoService.existsById(produto.getId()) ? new FechamentoCaixaItem(produto) : null));

        FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
        BeanUtils.copyProperties(fechamentoCaixaDto, fechamentoCaixa);
        fechamentoCaixa.setProdutos(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(fechamentoCaixaService.save(fechamentoCaixa));
    }

    @GetMapping("/buscaFechamentoCaixaProduto")
    public ResponseEntity<Object> buscaFechamentoCaixaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                              @PathVariable(value = "idProduto") Long idProduto,
                                                              @PathVariable(value = "dataInicial") String dataInicial,
                                                              @PathVariable(value = "dataFinal") String dataFinal){

        return null;
    }

    //Criar método de cancelar fechamento de caixa
}
