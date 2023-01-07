package com.parcao.security.services;

import com.parcao.models.FechamantoCaixaItemTela;
import com.parcao.models.FechamentoCaixa;

import java.util.List;

public interface FechamentoCaixaService {

    FechamentoCaixa save(FechamentoCaixa pedido);
    List<FechamantoCaixaItemTela> selectFechamentoCaixaProduto(Long filialId, Long produtoId, String dataInicial, String dataFinal);
}
