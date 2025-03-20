package com.parcao.controllers;

import com.parcao.model.dto.TaxaVendaDto;
import com.parcao.model.entity.TaxaVenda;
import com.parcao.services.TaxaVendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/taxavenda")
public class TaxaVendaController {

    final TaxaVendaService taxaVendaService;

    public TaxaVendaController(TaxaVendaService taxaVendaService) {
        this.taxaVendaService = taxaVendaService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTaxaVenda(@Valid @RequestBody TaxaVendaDto taxaVendaDto) {
        if (taxaVendaService.existsByNomeTaxa(taxaVendaDto.getNomeTaxa())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("TAXA_JA_CADASTRADA");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(taxaVendaService.save(taxaVendaDto));
    }

    @GetMapping("/list")
    public List<TaxaVenda> getAllTaxaVendas(){
        return new ArrayList<>(taxaVendaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaxaVenda(@PathVariable(value = "id") Long id){
        Optional<TaxaVenda> taxaVendaOptional = taxaVendaService.findById(id);
        if (!taxaVendaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAXA_NAO_ENCONTRADA");
        }
        return ResponseEntity.status(HttpStatus.OK).body(taxaVendaOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaxaVenda(@PathVariable (value = "id") Long id) {
        if (!taxaVendaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAXA_NAO_ENCONTRADA");
        }
        taxaVendaService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTaxaVenda(@PathVariable(value = "id") Long id, @Valid @RequestBody TaxaVendaDto taxaVendaDto) {
        Optional<TaxaVenda> taxaVendaOptional = taxaVendaService.findById(id);
        if (!taxaVendaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAXA_NAO_ENCONTRADA");
        }
        return  ResponseEntity.status(HttpStatus.OK).body(taxaVendaService.save(taxaVendaDto));
    }
}
