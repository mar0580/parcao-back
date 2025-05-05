package com.parcao.services;

import java.sql.Timestamp;
import java.util.List;

public interface IFechamentoCaixaItemService {
    List<Object[]> selectFechamentoCaixaProdutoDiario(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> selectFechamentoCaixaProdutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
}
