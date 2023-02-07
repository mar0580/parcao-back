package com.parcao.services;

import com.parcao.repository.VendaRepository;

import java.sql.Timestamp;

//@Service
public class VendaServiceImpl implements VendaService{
    final VendaRepository vendaRepository;

    public VendaServiceImpl(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Override
    public Object selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.selectSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);
    }

    @Override
    public Object selectSomatorioCustoProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.selectSomatorioCustoProduto(idFilial, idProduto, dataInicial, dataFinal);
    }
}
