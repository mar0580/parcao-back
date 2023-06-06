package com.parcao.services;

import com.parcao.dto.EstatisticaDto;

import java.text.ParseException;
import java.util.List;

public interface EstatisticaService {
    List<EstatisticaDto> selectEstatisticaPorTipoPagamento(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    /*
    List<Object[]> selectPerdasPorMes(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectPerdasPorProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectTotalVendasDiaria(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectTotalVendasMensais(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);

     */
}
