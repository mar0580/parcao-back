package com.parcao.controllers;

import com.parcao.dto.ControleDiarioEstoqueDto;
import com.parcao.dto.ControleDiarioValoresDto;
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

        List<Object[]> optionalSomatorioVendaProduto = vendaService.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        Object optionalValorBrutoPeriodo = vendaService.selectValorBrutoPeriodo(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));

        if (optionalSomatorioVendaProduto.size() > 0) {

            List<ControleDiarioValoresDto> customResponseList = new ArrayList();
            for (Object[] item: optionalSomatorioVendaProduto) {
                ControleDiarioValoresDto c = new ControleDiarioValoresDto();
                c.setValorUnitario(new BigDecimal(item[1].toString()));
                c.setValorTotalCustoUnitario(new BigDecimal(item[2].toString()));
                c.setValorTotalBruto(new BigDecimal(item[3].toString()));
                c.setValorTotalBrutoPeriodo(new BigDecimal(optionalValorBrutoPeriodo.toString()));
                c.setValorTotaLiquidoPeriodo(new BigDecimal(optionalValorBrutoPeriodo.toString()));//corrigir

                customResponseList.add(c);
            }
            return ResponseEntity.status(HttpStatus.OK).body(customResponseList.get(0));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("VALORES_VENDA_NAO_ENCONTRADO");
    }
}
