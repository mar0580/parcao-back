package com.parcao.service;

import com.parcao.model.dto.ControleDiarioEstoqueDTO;
import com.parcao.model.dto.FechamentoCaixaDTO;
import com.parcao.model.entity.FechamentoCaixa;

import java.text.ParseException;

public interface IFechamentoCaixaService {

    FechamentoCaixa save(FechamentoCaixa pedido);

    FechamentoCaixa createFechamentoCaixa(FechamentoCaixaDTO fechamentoCaixaDto);

    ControleDiarioEstoqueDTO buscaFechamentoCaixaProduto(Long idFilial, Long idProduto, String dataInicial, String dataFinal) throws ParseException;
}
