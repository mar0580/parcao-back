package com.parcao.controllers;

import com.parcao.dto.PedidoDto;
import com.parcao.dto.TaxaVendaDto;
import com.parcao.models.Pedido;
import com.parcao.models.TaxaVenda;
import com.parcao.security.services.PedidoService;
import com.parcao.security.services.TaxaVendaService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    final PedidoService pedidoService;


    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPedido(@Valid @RequestBody PedidoDto pedidoDto) {

        Pedido pedido = new Pedido();
        pedido.setDatePedido(LocalDateTime.now());
        pedido.setDateAtualizacao(LocalDateTime.now());
        BeanUtils.copyProperties(pedidoDto, pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.save(pedido));
    }
/*
    @GetMapping("/list")
    public List<TaxaVenda> getAllTaxaVendas(){
        List<TaxaVenda> taxaVenda = new ArrayList<TaxaVenda>();
        taxaVendaService.findAll().forEach(taxaVenda1 -> taxaVenda.add(taxaVenda1));
        return taxaVenda;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAXA_NAO_EXISTE");
        }
        taxaVendaService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTaxaVenda(@PathVariable(value = "id") Long id, @Valid @RequestBody TaxaVendaDto taxaVendaDto) {
        Optional<TaxaVenda> taxaVendaOptional = taxaVendaService.findById(id);
        if (!taxaVendaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TAXA_NAO_EXISTE");
        }
        TaxaVenda taxaVenda = new TaxaVenda();
        BeanUtils.copyProperties(taxaVendaDto, taxaVenda);
        taxaVenda.setId(taxaVendaOptional.get().getId());
        taxaVenda.setDateAtualizacao(LocalDateTime.now());
        return  ResponseEntity.status(HttpStatus.OK).body(taxaVendaService.save(taxaVenda));
    }*/
}
