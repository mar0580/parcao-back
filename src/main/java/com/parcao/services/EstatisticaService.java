package com.parcao.services;

import java.sql.Timestamp;
import java.util.List;

public interface EstatisticaService {
    List<Object[]> selectEstatisticaPorTipoPagamento(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectPerdasPorMes(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectPerdasPorProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
}
