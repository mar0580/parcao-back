package com.parcao.controller;

import com.parcao.dto.EstatisticaDTO;
import com.parcao.service.IEstatisticaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/estatistica")
public class EstatisticaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstatisticaController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IEstatisticaService estatisticaService;

    public EstatisticaController(IEstatisticaService estatisticaService) {
        this.estatisticaService = estatisticaService;
    }

    @GetMapping("/buscaEstatisticaPorTipoPagamento/{idFilial}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaEstatisticaPorTipoPagamento(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando estatística por tipo de pagamento para filial: {}", correlationId, idFilial);

        List<EstatisticaDTO> resultado = estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal);

        if (!resultado.isEmpty()) {
            LOGGER.info("[correlationId={}] Estatística encontrada com {} registros", correlationId, resultado.size());
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        }

        LOGGER.info("[correlationId={}] Nenhum tipo de pagamento encontrado", correlationId);
        return ResponseEntity.status(HttpStatus.OK).body("TIPO_DE_PAGAMENTO_NA0_ENCONTRADO");
    }

    @GetMapping("/buscaPerdasPorMes/{idFilial}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaPerdasPorMes(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando perdas por mês para filial: {}", correlationId, idFilial);

        List<EstatisticaDTO> resultado = estatisticaService.selectPerdasPorMes(idFilial, dataInicial, dataFinal);

        if (!resultado.isEmpty()) {
            LOGGER.info("[correlationId={}] Perdas encontradas com {} registros", correlationId, resultado.size());
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        }

        LOGGER.info("[correlationId={}] Sem dados estatísticos de perdas por mês", correlationId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
    }

    @GetMapping("/buscaPerdasPorProduto/{idFilial}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaPerdasPorProduto(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando perdas por produto para filial: {}", correlationId, idFilial);

        List<EstatisticaDTO> resultado = estatisticaService.selectPerdasPorProduto(idFilial, dataInicial, dataFinal);

        if (!resultado.isEmpty()) {
            LOGGER.info("[correlationId={}] Perdas por produto encontradas com {} registros", correlationId, resultado.size());
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        }

        LOGGER.info("[correlationId={}] Sem dados estatísticos de perdas por produto", correlationId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
    }

    @GetMapping("/buscaEstatisticaVendasDiaria/{idFilial}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaEstatisticaVendasDiaria(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando estatística de vendas diárias para filial: {}", correlationId, idFilial);

        List<EstatisticaDTO> resultado = estatisticaService.selectTotalVendasDiaria(idFilial, dataInicial, dataFinal);

        if (!resultado.isEmpty()) {
            LOGGER.info("[correlationId={}] Estatísticas de vendas diárias encontradas com {} registros", correlationId, resultado.size());
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        }

        LOGGER.info("[correlationId={}] Sem dados estatísticos de vendas diárias", correlationId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
    }

    @GetMapping("/buscaEstatisticaVendasMensais/{idFilial}/{dataInicial}/{dataFinal}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> buscaEstatisticaVendasMensais(
            @PathVariable(value = "idFilial") Long idFilial,
            @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
            @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando estatística de vendas mensais para filial: {}", correlationId, idFilial);

        List<EstatisticaDTO> resultado = estatisticaService.selectTotalVendasMensais(idFilial, dataInicial, dataFinal);

        if (!resultado.isEmpty()) {
            LOGGER.info("[correlationId={}] Estatísticas de vendas mensais encontradas com {} registros", correlationId, resultado.size());
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        }

        LOGGER.info("[correlationId={}] Sem dados estatísticos de vendas mensais", correlationId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
