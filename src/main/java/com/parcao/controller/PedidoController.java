package com.parcao.controller;

import com.parcao.dto.PedidoDTO;
import com.parcao.model.Pedido;
import com.parcao.service.IPedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 13600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IPedidoService pedidoService;

    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> createPedido(@Valid @RequestBody PedidoDTO pedidoDto) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar pedido", correlationId);

        Pedido pedido = pedidoService.save(pedidoDto);

        LOGGER.info("[correlationId={}] Pedido criado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }

    //Criar método de cancelar pedido
}
