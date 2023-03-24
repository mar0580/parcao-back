package com.parcao.services;

import java.sql.Timestamp;
import java.util.List;

public interface EstatisticaService {
    List<Object[]> selectEstatisticaPorTipoPagamento(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
}
