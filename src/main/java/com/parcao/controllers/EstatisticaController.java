package com.parcao.controllers;

import com.parcao.services.EstatisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
        if(estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal).size() > 0){
            return ResponseEntity.status(HttpStatus.OK).body(estatisticaService.selectEstatisticaPorTipoPagamento(idFilial, dataInicial, dataFinal));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("TIPO_DE_PAGAMENTO_NA0_ENCONTRADO");
        }
    }

    @GetMapping("/buscaPerdasPorMes/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaPerdasPorMes(@PathVariable(value = "idFilial") Long idFilial,
                                                             @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                             @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        if(estatisticaService.selectPerdasPorMes(idFilial, dataInicial, dataFinal).size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(estatisticaService.selectPerdasPorMes(idFilial, dataInicial, dataFinal));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }

    @GetMapping("/buscaPerdasPorProduto/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaPerdasPorProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                    @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                    @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        if(estatisticaService.selectPerdasPorProduto(idFilial, dataInicial, dataFinal).size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(estatisticaService.selectPerdasPorProduto(idFilial, dataInicial, dataFinal));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }
    /*
    @GetMapping("/buscaEstatisticaVendasDiaria/{idFilial}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaEstatisticaVendasDiaria(@PathVariable(value = "idFilial") Long idFilial,
                                                               @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataInicial,
                                                               @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {

        if(estatisticaService.selectTotalVendasDiaria(idFilial, dataInicial, dataFinal).size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(estatisticaService.selectTotalVendasDiaria(idFilial, dataInicial, dataFinal));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SEM_DADOS_ESTATISTICOS");
        }
    }
/*
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
