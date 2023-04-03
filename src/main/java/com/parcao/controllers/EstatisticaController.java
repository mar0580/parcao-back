package com.parcao.controllers;

import com.parcao.dto.ControleDiarioValoresDto;
import com.parcao.dto.EstatisticaDto;
import com.parcao.services.EstatisticaService;
import com.parcao.utils.Util;
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

    final EstatisticaService estatisticaService;

    public EstatisticaController(EstatisticaService estatisticaService) {
        this.estatisticaService = estatisticaService;
    }

    @GetMapping("/buscaEstatisticaPorTipoPagamento/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaEstatisticaPorTipoPagamento(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        List<Object[]> estatisticaPorTipoPagamento = estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaPorTipoPagamento){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setEstatisticaNomeTaxa(item[0].toString());
            estatisticaDto.setEstatisticaTotalVendaTipoPagamento((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        if(listEstatisticas.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(listEstatisticas);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

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
}
