package com.parcao.services;

import com.parcao.model.dto.EstatisticaDto;

import java.text.ParseException;
import java.util.List;

public interface EstatisticaService {
    List<EstatisticaDto> selectEstatisticaPorTipoPagamento(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDto> selectPerdasPorMes(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDto> selectPerdasPorProduto(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDto> selectTotalVendasDiaria(Long idFilial, String dataInicial, String dataFinal)  throws ParseException;
    List<EstatisticaDto> selectTotalVendasMensais(Long idFilial, String dataInicial, String dataFinal)  throws ParseException;
}
