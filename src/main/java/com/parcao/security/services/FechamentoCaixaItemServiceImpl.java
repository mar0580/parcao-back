package com.parcao.security.services;

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
    public List<Object[]> selectFechamentoCaixaProduto_(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return fechamentoCaixaItemRepository.selectFechamentoCaixaProduto_(idFilial, idProduto, dataInicial, dataFinal);
    }
}
