package com.parcao.service;

import com.parcao.dto.ControleDiarioEstoqueDTO;
import com.parcao.dto.FechamentoCaixaDTO;
import com.parcao.model.FechamentoCaixa;

import java.text.ParseException;

public interface IFechamentoCaixaService {

    FechamentoCaixa save(FechamentoCaixa pedido);

    FechamentoCaixa createFechamentoCaixa(FechamentoCaixaDTO fechamentoCaixaDto);

    ControleDiarioEstoqueDTO buscaFechamentoCaixaProduto(Long idFilial, Long idProduto, String dataInicial, String dataFinal) throws ParseException;
}
