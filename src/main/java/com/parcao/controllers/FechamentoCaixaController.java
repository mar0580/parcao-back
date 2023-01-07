package com.parcao.controllers;

import com.parcao.dto.FechamentoCaixaDto;
import com.parcao.dto.FechamentoCaixaItemDto;
import com.parcao.models.FechamantoCaixaItemTela;
import com.parcao.models.FechamentoCaixa;
import com.parcao.models.FechamentoCaixaItem;
import com.parcao.security.services.FechamentoCaixaService;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/buscaFechamentoCaixaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaFechamentoCaixaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                              @PathVariable(value = "idProduto") Long idProduto,
                                                              @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd")  String dataInicial,
                                                              @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) {
        List<FechamantoCaixaItemTela> optionalFechamantoCaixaItemTela = fechamentoCaixaService.selectFechamentoCaixaProduto(idFilial, idProduto, dataInicial.concat(" 00:00:00.000"), dataFinal.concat("  23:59:59.999"));
        if (optionalFechamantoCaixaItemTela.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalFechamantoCaixaItemTela);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FECHAMENTO_CAIXA_NAO_ENCONTRADO");
    }

    //Criar método de cancelar fechamento de caixa
}
