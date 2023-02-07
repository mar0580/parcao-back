package com.parcao.controllers;

import com.parcao.dto.ControleDiarioDto;
import com.parcao.dto.PedidoDto;
import com.parcao.services.VendaService;
import com.parcao.utils.Util;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/venda")
public class VendaController {
    final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping("/buscaSomatorioVendaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaSomatorioVendaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                              @PathVariable(value = "idProduto") Long idProduto,
                                                              @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd")  String dataInicial,
                                                              @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setValorTotal((BigDecimal) vendaService.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal)));
        return ResponseEntity.status(HttpStatus.OK).body(pedidoDto);
    }
}
