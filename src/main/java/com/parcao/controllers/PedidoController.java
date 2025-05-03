package com.parcao.controllers;

import com.parcao.model.dto.PedidoDTO;
import com.parcao.services.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 13600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    final PedidoService pedidoService;


    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPedido(@Valid @RequestBody PedidoDTO pedidoDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedidoDto));
    }

    //Criar método de cancelar pedido
}
