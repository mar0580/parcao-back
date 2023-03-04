package com.parcao.controllers;

import com.parcao.dto.ControleDiarioValoresDto;
import com.parcao.services.FechamentoCaixaItemService;
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
    final FechamentoCaixaItemService fechamentoCaixaItemService;

    public VendaController(VendaService vendaService, FechamentoCaixaItemService fechamentoCaixaItemService) {
        this.vendaService = vendaService;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
    }

    @GetMapping("/buscaSomatorioVendaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaSomatorioVendaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                              @PathVariable(value = "idProduto") Long idProduto,
                                                              @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd")  String dataInicial,
                                                              @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> optionalFechamentoCaixaItem = null;
        if(dataInicial.compareTo(dataFinal) == 0) {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoDiario(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        } else {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoPeriodo(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        }

        if (optionalFechamentoCaixaItem.size() > 0) {
            ControleDiarioValoresDto c = new ControleDiarioValoresDto();

            List<ControleDiarioValoresDto> customResponseList = new ArrayList();
            for (Object[] item : optionalFechamentoCaixaItem) {

                BigInteger b = new BigInteger(item[0].toString());
                c.setId(b.longValue());
                c.setInicio((int) item[1]);
                c.setEntrada((int) item[2]);
                c.setPerda((int) item[3]);
                c.setQuantidadeFinal((int) item[4]);
                c.setSaida((int) item[5]);
                if(dataInicial.compareTo(dataFinal) == 0) {
                    c.setObservacao(((String) item[6]));
                } else {
                    c.setObservacao(" ");
                }
                customResponseList.add(c);
            }


            List<Object[]> optionalSomatorioVendaProduto = vendaService.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object optionalValorBrutoPeriodo = vendaService.selectValorBrutoPeriodo(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));

            if (optionalSomatorioVendaProduto.size() > 0) {
                for (Object[] item : optionalSomatorioVendaProduto) {

                    c.setValorUnitario(new BigDecimal(item[1].toString()));// precos-ok
                    c.setValorTotalCustoUnitario(new BigDecimal(item[2].toString()));//somatorio dos   custos-ok
                    c.setValorTotalBruto( (new BigDecimal(item[4].toString()).subtract((new BigDecimal(item[5].toString()).multiply(BigDecimal.valueOf(c.getPerda() + c.getSaida()))))));//total_custos-ok

                    //

                    c.setValorTotalBrutoPeriodo(new BigDecimal(optionalValorBrutoPeriodo.toString()));
                    //c.setValorTotaLiquidoPeriodo(new BigDecimal(optionalValorBrutoPeriodo.toString()));//corrigir

                    customResponseList.add(c);
                }
                return ResponseEntity.status(HttpStatus.OK).body(customResponseList.get(0));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FECHAMENTO_CAIXA_NAO_ENCONTRADO");
    }
}
