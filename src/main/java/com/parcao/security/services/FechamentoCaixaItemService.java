package com.parcao.security.services;

import java.sql.Timestamp;
import java.util.List;

public interface FechamentoCaixaItemService{
    List<Object[]> selectFechamentoCaixaProduto_(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
}
