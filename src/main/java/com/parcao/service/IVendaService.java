package com.parcao.service;

import com.parcao.model.dto.ControleDiarioValoresDTO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

public interface IVendaService {

    List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    Object selectValorTotalCocoCopoGarrafa(Long idFilial, String descricaoProduto, Timestamp dataInicial, Timestamp dataFinal);
    List<Object[]> somatorioTotalBrutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    Object somatorioTotalLiquidoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);

    Object totalCustosCoco(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);

    ControleDiarioValoresDTO buscaSomatorioVendaProduto(Long idFilial, Long idProduto, String dataInicial, String dataFinal) throws ParseException;
}
