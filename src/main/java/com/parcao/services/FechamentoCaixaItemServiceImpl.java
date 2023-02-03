package com.parcao.services;

import com.parcao.repository.FechamentoCaixaItemRepository;

import java.sql.Timestamp;
import java.util.List;

//@Service
public class FechamentoCaixaItemServiceImpl implements FechamentoCaixaItemService{
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
