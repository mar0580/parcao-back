package com.parcao.services;

import com.parcao.model.dto.EstatisticaDTO;

import java.text.ParseException;
import java.util.List;

public interface EstatisticaService {
    List<EstatisticaDTO> selectEstatisticaPorTipoPagamento(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDTO> selectPerdasPorMes(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDTO> selectPerdasPorProduto(Long idFilial, String dataInicial, String dataFinal) throws ParseException;
    List<EstatisticaDTO> selectTotalVendasDiaria(Long idFilial, String dataInicial, String dataFinal)  throws ParseException;
    List<EstatisticaDTO> selectTotalVendasMensais(Long idFilial, String dataInicial, String dataFinal)  throws ParseException;
}
