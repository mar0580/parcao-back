package com.parcao.controllers;

import com.parcao.dto.EstatisticaDto;
import com.parcao.services.ClienteService;
import com.parcao.services.EstatisticaService;
import com.parcao.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/estatistica")
public class EstatisticaController {
    @Autowired
    private EstatisticaService estatisticaService;


    @GetMapping("/buscaEstatisticaPorTipoPagamento/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaEstatisticaPorTipoPagamento(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
/*
        List<Object[]> estatisticaPorTipoPagamento = estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaPorTipoPagamento){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setEstatisticaNomeTaxa(item[0].toString());
            estatisticaDto.setEstatisticaQuantidadeVendaTipoPagamento((int)item[1]);
            estatisticaDto.setEstatisticaValorTotalVendaTipoPagamento((int)item[2]);
            listEstatisticas.add(estatisticaDto);
        }
 */

        return ResponseEntity.status(HttpStatus.OK).body(estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal).size() > 0);

    }
    /*
    @GetMapping("/buscaPerdasPorMes/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaPerdasPorMes(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        List<Object[]> perdasPorMes = estatisticaService.selectPerdasPorMes(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorMes){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setMes(item[0].toString());
            estatisticaDto.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        if(listEstatisticas.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(listEstatisticas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

    @GetMapping("/buscaPerdasPorProduto/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaPerdasPorProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                    @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                    @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        List<Object[]> perdasPorProduto = estatisticaService.selectPerdasPorProduto(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorProduto){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setNomeProduto(item[0].toString());
            estatisticaDto.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        if(listEstatisticas.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(listEstatisticas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

    @GetMapping("/buscaEstatisticaVendasDiaria/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaEstatisticaVendasDiaria(@PathVariable(value = "idFilial") Long idFilial,
                                                               @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                               @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> estatisticaTotalVendasDiaria = estatisticaService.selectTotalVendasDiaria(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaTotalVendasDiaria){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setDiaMes(item[0].toString());
            estatisticaDto.setEstatisticaValorTotalVendaTipoPagamento((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        if(listEstatisticas.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(listEstatisticas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

    @GetMapping("/buscaEstatisticaVendasMensais/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaEstatisticaVendasMensais(@PathVariable(value = "idFilial") Long idFilial,
                                                               @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                               @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> estatisticaTotalVendasMensais = estatisticaService.selectTotalVendasMensais(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaTotalVendasMensais){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setMes(item[0].toString());
            estatisticaDto.setEstatisticaValorTotalVendaTipoPagamento((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        if(listEstatisticas.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(listEstatisticas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

     */
}
