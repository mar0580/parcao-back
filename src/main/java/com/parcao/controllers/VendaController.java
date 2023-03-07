package com.parcao.controllers;

import com.google.common.base.Strings;
import com.parcao.dto.ControleDiarioValoresDto;
import com.parcao.models.Produto;
import com.parcao.services.FechamentoCaixaItemService;
import com.parcao.services.ProdutoService;
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

    final ProdutoService produtoService;
    final FechamentoCaixaItemService fechamentoCaixaItemService;

    static final String COPO = "copo";

    static final String GARRAFA = "garrafa";

    public VendaController(VendaService vendaService, ProdutoService produtoService, FechamentoCaixaItemService fechamentoCaixaItemService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
    }

    @GetMapping("/buscaSomatorioVendaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaSomatorioVendaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "idProduto") Long idProduto,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> optionalFechamentoCaixaItem = null;
        if (dataInicial.compareTo(dataFinal) == 0) {
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
                if (dataInicial.compareTo(dataFinal) == 0) {
                    c.setObservacao(((String) item[6]));
                } else {
                    c.setObservacao(" ");
                }
                customResponseList.add(c);
            }

            List<Object[]> optionalSomatorioVendaProduto = vendaService.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object optionalValorBrutoPeriodo = vendaService.selectValorBrutoPeriodo(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object optionalValorTotalCocoCopo = vendaService.selectValorTotalCocoCopoGarrafa(idFilial, COPO, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object optionalValorTotalCocoGarrafa = vendaService.selectValorTotalCocoCopoGarrafa(idFilial, GARRAFA, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));

            List<Produto> produtos = produtoService.findAll();
            BigDecimal valorTotal = BigDecimal.ZERO;
            for (Produto p : produtos) {//problema
                List<Object[]> somatorioTotalBrutoPeriodo = vendaService.somatorioTotalBrutoPeriodo(idFilial, p.getId(), Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
                for (Object[] item : somatorioTotalBrutoPeriodo) {
                    if(!Strings.isNullOrEmpty(item[3].toString())) {
                        valorTotal = valorTotal.add(new BigDecimal(item[3].toString()).multiply(new BigDecimal(item[2].toString())));
                    }
                }
            }

            if (optionalSomatorioVendaProduto.size() > 0) {
                for (Object[] item : optionalSomatorioVendaProduto) {

                    c.setPreco(new BigDecimal(item[1].toString()));// precos-ok
                    c.setCusto(new BigDecimal(item[2].toString()));//somatorio dos   custos-ok
                    c.setTotalCusto((new BigDecimal(item[4].toString()).subtract((new BigDecimal(item[5].toString()).multiply(BigDecimal.valueOf(c.getPerda() + c.getSaida()))))));//total_custos-ok
                    c.setTotalCoco(new BigDecimal(optionalValorTotalCocoCopo.toString()).add(new BigDecimal(optionalValorTotalCocoGarrafa.toString()))); //total_coco

                    c.setValorTotalBrutoPeriodo(valorTotal);// total_bruto ok
                    c.setValorTotaLiquidoPeriodo(new BigDecimal(optionalValorBrutoPeriodo.toString()));//total_liquido nok

                    customResponseList.add(c);
                }
                return ResponseEntity.status(HttpStatus.OK).body(customResponseList.get(0));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FECHAMENTO_CAIXA_NAO_ENCONTRADO");
    }
}
