package com.parcao.services;

import java.sql.Timestamp;
import java.util.List;

public interface IVendaService {

    List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    Object selectValorTotalCocoCopoGarrafa(Long idFilial, String descricaoProduto, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> somatorioTotalBrutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    Object somatorioTotalLiquidoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);

    Object totalCustosCoco(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
}
