package com.parcao.controllers;

import com.parcao.dto.PedidoDto;
import com.parcao.dto.PedidoItemDto;
import com.parcao.models.Pedido;
import com.parcao.models.PedidoItem;
import com.parcao.services.PedidoService;
import com.parcao.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    final PedidoService pedidoService;
    final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPedido(@Valid @RequestBody PedidoDto pedidoDto) {
        Set<PedidoItemDto> produtoItemDto = pedidoDto.getProdutos();
        Set<PedidoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(produtoService.existsById(produto.getId()) ? new PedidoItem(produto, produtoService) : null));

        Pedido pedido = new Pedido();
        BeanUtils.copyProperties(pedidoDto, pedido);
        pedido.setProdutos(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }

    //Criar método de cancelar pedido
}
