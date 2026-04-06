package com.parcao.controller;

import com.parcao.dto.AbastecimentoDTO;
import com.parcao.service.IAbastecimentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/abastecimento")
public class AbastecimentoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbastecimentoController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IAbastecimentoService abastecimentoService;

    public AbastecimentoController(IAbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> createAbastecimento(@Valid @RequestBody AbastecimentoDTO abastecimentoDto) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar abastecimento", correlationId);

        Object result = abastecimentoService.createAbastecimento(abastecimentoDto);

        LOGGER.info("[correlationId={}] Abastecimento criado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> updateEstoqueFilial(@Valid @RequestBody AbastecimentoDTO abastecimentoDto) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar estoque da filial", correlationId);

        String result = abastecimentoService.updateEstoqueFilial(abastecimentoDto);

        LOGGER.info("[correlationId={}] Estoque da filial atualizado", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
