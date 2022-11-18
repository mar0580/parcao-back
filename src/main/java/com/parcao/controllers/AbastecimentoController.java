package com.parcao.controllers;

import com.parcao.dto.AbastecimentoDto;
import com.parcao.dto.AbastecimentoItemDto;
import com.parcao.dto.PedidoDto;
import com.parcao.dto.PedidoItemDto;
import com.parcao.models.Abastecimento;
import com.parcao.models.AbastecimentoItem;
import com.parcao.models.Pedido;
import com.parcao.models.PedidoItem;
import com.parcao.security.services.AbastecimentoService;
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
@RequestMapping("/api/abastecimento")
public class AbastecimentoController {
    final AbastecimentoService abastecimentoService;
    final ProdutoService produtoService;

    public AbastecimentoController(AbastecimentoService abastecimentoService, ProdutoService produtoService) {
        this.abastecimentoService = abastecimentoService;
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAbastecimento(@Valid @RequestBody AbastecimentoDto abastecimentoDto) {
        Set<AbastecimentoItemDto> produtoItemDto = abastecimentoDto.getProdutos();
        Set<AbastecimentoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(
                produtoService.existsById(produto.getId()) ? new AbastecimentoItem(produto) : null));

        Abastecimento abastecimento = new Abastecimento();
        BeanUtils.copyProperties(abastecimentoDto, abastecimento);
        abastecimento.setProdutos(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(abastecimentoService.save(abastecimento));
    }
}
