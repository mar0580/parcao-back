package com.parcao.controller;

import com.parcao.dto.ControleDiarioValoresDTO;
import com.parcao.service.IVendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/venda")
public class VendaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendaController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IVendaService vendaService;

    public VendaController(IVendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping("/buscaSomatorioVendaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaSomatorioVendaProduto(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "idProduto") Long idProduto,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para buscar somatório de vendas", correlationId);

        ControleDiarioValoresDTO resultado = vendaService.buscaSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);

        LOGGER.info("[correlationId={}] Somatório de vendas retornado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
