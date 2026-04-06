package com.parcao.controller;

import com.parcao.model.dto.TaxaVendaDTO;
import com.parcao.model.entity.TaxaVenda;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.service.ITaxaVendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/taxavenda")
public class TaxaVendaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxaVendaController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final ITaxaVendaService taxaVendaService;

    public TaxaVendaController(ITaxaVendaService taxaVendaService) {
        this.taxaVendaService = taxaVendaService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxaVenda> createTaxaVenda(@Valid @RequestBody TaxaVendaDTO taxaVendaDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar taxa de venda", correlationId);

        TaxaVenda taxaVenda = taxaVendaService.createTaxaVenda(taxaVendaDTO);

        LOGGER.info("[correlationId={}] Taxa de venda criada com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(taxaVenda);
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<TaxaVenda> getAllTaxaVendas() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Listando todas as taxas de venda", correlationId);
        return taxaVendaService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaxaVenda> getTaxaVenda(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando taxa de venda: {}", correlationId, id);

        TaxaVenda taxaVenda = taxaVendaService.getTaxaVendaById(id);
        return ResponseEntity.ok(taxaVenda);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTaxaVenda(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para excluir taxa de venda: {}", correlationId, id);

        taxaVendaService.deleteTaxaVenda(id);

        LOGGER.info("[correlationId={}] Taxa de venda excluída com sucesso", correlationId);
        return ResponseEntity.ok(MensagemEnum.SUCESSO.getMensagem());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxaVenda> updateTaxaVenda(@PathVariable(value = "id") Long id, @Valid @RequestBody TaxaVendaDTO taxaVendaDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar taxa de venda: {}", correlationId, id);

        TaxaVenda taxaVenda = taxaVendaService.updateTaxaVenda(id, taxaVendaDTO);

        LOGGER.info("[correlationId={}] Taxa de venda atualizada com sucesso", correlationId);
        return ResponseEntity.ok(taxaVenda);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
