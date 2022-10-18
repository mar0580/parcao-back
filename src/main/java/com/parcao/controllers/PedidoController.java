package com.parcao.controllers;

import com.parcao.dto.PedidoDto;
import com.parcao.models.Pedido;
import com.parcao.models.PedidoItem;
import com.parcao.security.services.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPedido(@Valid @RequestBody PedidoDto pedidoDto) {


        Set<PedidoItem> produtos = new HashSet<>();
        produtos.add(new PedidoItem(1L, "COPO 200ML", 1, new BigDecimal("0.99"), new BigDecimal("1.99")));

        Pedido pedido = new Pedido("CREDITO", 2L, 3L, new BigDecimal("9.99"), 4L, 5L, produtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));

/*
        produto.setDescricaoProduto("COPO 200ML");
        produto.setQuantidade(1);
        produto.setValorUnitario(new BigDecimal("0.99"));
        produto.setValorUnitario(new BigDecimal("1.99"));

        pedido.setTpPagamento("CREDITO");
        pedido.setIdFilial(1L);
        pedido.setIdUser(1L);
        pedido.setValorTotal(new BigDecimal("0.99"));
        pedido.setIdCliente(1L);
        pedido.setIdTaxaVenda(1L);
        return null;


 */

    }
}
