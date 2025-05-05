package com.parcao.services.impl;

import com.parcao.dao.FechamentoCaixaItemRepository;
import com.parcao.services.IFechamentoCaixaItemService;

import java.sql.Timestamp;
import java.util.List;

//@Service
public class FechamentoCaixaItemServiceImpl implements IFechamentoCaixaItemService {
    final FechamentoCaixaItemRepository fechamentoCaixaItemRepository;

    public FechamentoCaixaItemServiceImpl(FechamentoCaixaItemRepository fechamentoCaixaItemRepository) {
        this.fechamentoCaixaItemRepository = fechamentoCaixaItemRepository;
    }

    @Override
    public List<Object[]> selectFechamentoCaixaProdutoDiario(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return fechamentoCaixaItemRepository.selectFechamentoCaixaProdutoDiario(idFilial, idProduto, dataInicial, dataFinal);
    }

    public List<Object[]> selectFechamentoCaixaProdutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return fechamentoCaixaItemRepository.selectFechamentoCaixaProdutoPeriodo(idFilial, idProduto, dataInicial, dataFinal);
    }
}
