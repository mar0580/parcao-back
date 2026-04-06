package com.parcao.service;

import java.sql.Timestamp;
import java.util.List;

public interface ISchedulerService {
    List<Object[]> countVendasByPagamentoPeriodo(Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> vendasDetalhadasPorFiialandProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    Object vendaTotalAtualPorFilial(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
}
