package com.parcao.controllers;

import com.google.common.base.Strings;
import com.parcao.model.dto.ControleDiarioValoresDTO;
import com.parcao.model.entity.Produto;
import com.parcao.services.IFechamentoCaixaItemService;
import com.parcao.services.IProdutoService;
import com.parcao.services.IVendaService;
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
    final IVendaService vendaService;

    final IProdutoService produtoService;
    final IFechamentoCaixaItemService fechamentoCaixaItemService;

    static final String COPO = "copo";

    static final String GARRAFA = "garrafa";

    public VendaController(IVendaService vendaService, IProdutoService produtoService, IFechamentoCaixaItemService fechamentoCaixaItemService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
    }

    @GetMapping("/buscaSomatorioVendaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaSomatorioVendaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "idProduto") Long idProduto,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> fechamentoCaixaItemProduto = null;
        if (dataInicial.compareTo(dataFinal) == 0) {
            fechamentoCaixaItemProduto = fechamentoCaixaItemService.selectFechamentoCaixaProdutoDiario(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        } else {
            fechamentoCaixaItemProduto = fechamentoCaixaItemService.selectFechamentoCaixaProdutoPeriodo(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        }

        if (fechamentoCaixaItemProduto.size() > 0) {
            ControleDiarioValoresDTO c = new ControleDiarioValoresDTO();

            List<ControleDiarioValoresDTO> customResponseList = new ArrayList();
            for (Object[] itemFechamentoCaixaItemProduto : fechamentoCaixaItemProduto) {

                BigInteger b = new BigInteger(itemFechamentoCaixaItemProduto[0].toString());
                c.setId(b.longValue());
                c.setInicio((int) itemFechamentoCaixaItemProduto[1]);
                c.setEntrada((int) itemFechamentoCaixaItemProduto[2]);
                c.setPerda((int) itemFechamentoCaixaItemProduto[3]);
                c.setQuantidadeFinal((int) itemFechamentoCaixaItemProduto[4]);
                c.setSaida((int) itemFechamentoCaixaItemProduto[5]);
                if (dataInicial.compareTo(dataFinal) == 0) {
                    c.setObservacao(((String) itemFechamentoCaixaItemProduto[6]));
                } else {
                    c.setObservacao(" ");
                }
                customResponseList.add(c);
            }

            List<Produto> produtos = produtoService.findAll();
            BigDecimal valorTotalBruto = BigDecimal.ZERO;
            BigDecimal valorTotalLiquido = BigDecimal.ZERO;
            for (Produto p : produtos) {
                List<Object[]> somatorioTotalBrutoPeriodo = vendaService.somatorioTotalBrutoPeriodo(idFilial, p.getId(), Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
                for (Object[] itemSomatorioTotalBrutoPeriodo : somatorioTotalBrutoPeriodo) {
                    if(!Strings.isNullOrEmpty(itemSomatorioTotalBrutoPeriodo[3].toString())) {
                        valorTotalBruto = valorTotalBruto.add(new BigDecimal(itemSomatorioTotalBrutoPeriodo[3].toString()).multiply(new BigDecimal(itemSomatorioTotalBrutoPeriodo[2].toString())));
                    }
                }
                Object somatorioTotalLiquidoPeriodo = vendaService.somatorioTotalLiquidoPeriodo(idFilial, p.getId(), Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
                valorTotalLiquido = valorTotalLiquido.add(new BigDecimal(somatorioTotalLiquidoPeriodo.toString()));
            }

            List<Object[]> somatorioVendaProduto = vendaService.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object valorTotalCocoCopoGarrafa = vendaService.selectValorTotalCocoCopoGarrafa(idFilial, COPO, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object valorTotalCocoGarrafa = vendaService.selectValorTotalCocoCopoGarrafa(idFilial, GARRAFA, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            Object totalCustosCoco = vendaService.totalCustosCoco(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            if (somatorioVendaProduto.size() > 0) {
                for (Object[] itemSomatorioVendaProduto : somatorioVendaProduto) {

                    c.setPreco(new BigDecimal(itemSomatorioVendaProduto[1].toString()));// precos-ok
                    c.setCusto(new BigDecimal(itemSomatorioVendaProduto[2].toString()));//somatorio dos   custos-ok
                    c.setTotalCusto((new BigDecimal(itemSomatorioVendaProduto[4].toString()).subtract((new BigDecimal(itemSomatorioVendaProduto[5].toString()).multiply(BigDecimal.valueOf(c.getPerda() + c.getSaida()))))));//total_custos-ok
                    c.setTotalCoco(new BigDecimal(valorTotalCocoCopoGarrafa.toString()).add(new BigDecimal(valorTotalCocoGarrafa.toString()))); //total_coco
                    c.setTotal(new BigDecimal(itemSomatorioVendaProduto[1].toString()).multiply(BigDecimal.valueOf(c.getSaida()))); //total-ok
                    c.setValorTotalBrutoPeriodo(valorTotalBruto);// total_bruto ok
                    c.setValorTotaLiquidoPeriodo(valorTotalLiquido.subtract(new BigDecimal(totalCustosCoco.toString())));//total_liquido nok
                    customResponseList.add(c);
                }
                return ResponseEntity.status(HttpStatus.OK).body(customResponseList.get(0));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FECHAMENTO_CAIXA_NAO_ENCONTRADO");
    }
}
