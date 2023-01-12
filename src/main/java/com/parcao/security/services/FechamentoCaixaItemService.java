package com.parcao.security.services;

import com.parcao.dto.FechamentoCaixaItemDto;

import java.sql.Timestamp;
import java.util.List;

public interface FechamentoCaixaItemService{
    List<Object> selectFechamentoCaixaProduto_(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
}
