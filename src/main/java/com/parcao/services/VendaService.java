package com.parcao.services;

import com.parcao.models.Abastecimento;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface VendaService {

    List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
    Object selectValorBrutoPeriodo(Long idFilial, Timestamp dataInicial, Timestamp dataFinal);
    Object selectValorTotalCocoCopoGarrafa(Long idFilial, String descricaoProduto, Timestamp dataInicial, Timestamp dataFinal);
}
