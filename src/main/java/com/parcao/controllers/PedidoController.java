package com.parcao.controllers;

import com.parcao.model.dto.PedidoDto;
import com.parcao.model.dto.PedidoItemDto;
import com.parcao.model.entity.Pedido;
import com.parcao.model.entity.PedidoItem;
import com.parcao.services.PedidoService;
import com.parcao.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 13600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    final PedidoService pedidoService;


    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPedido(@Valid @RequestBody PedidoDto pedidoDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedidoDto));
    }

    //Criar método de cancelar pedido
}
