package com.parcao.controller;

import com.parcao.dto.ControleDiarioEstoqueDTO;
import com.parcao.dto.FechamentoCaixaDTO;
import com.parcao.model.FechamentoCaixa;
import com.parcao.service.IFechamentoCaixaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fechamentocaixa")
public class FechamentoCaixaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoCaixaController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IFechamentoCaixaService fechamentoCaixaService;

    public FechamentoCaixaController(IFechamentoCaixaService fechamentoCaixaService) {
        this.fechamentoCaixaService = fechamentoCaixaService;
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FechamentoCaixa> createFechamentoCaixa(@Valid @RequestBody FechamentoCaixaDTO fechamentoCaixaDto) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar fechamento de caixa", correlationId);

        FechamentoCaixa fechamentoCaixa = fechamentoCaixaService.createFechamentoCaixa(fechamentoCaixaDto);

        LOGGER.info("[correlationId={}] Fechamento de caixa criado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fechamentoCaixa);
    }

    @GetMapping("/buscaFechamentoCaixaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaFechamentoCaixaProduto(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "idProduto") Long idProduto,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para buscar fechamento de caixa", correlationId);

        ControleDiarioEstoqueDTO resultado = fechamentoCaixaService.buscaFechamentoCaixaProduto(idFilial, idProduto, dataInicial, dataFinal);

        LOGGER.info("[correlationId={}] Fechamento de caixa retornado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }

    //Criar método de cancelar fechamento de caixa
}
