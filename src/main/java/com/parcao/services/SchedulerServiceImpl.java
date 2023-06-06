package com.parcao.services;

import com.parcao.dao.SchedulerRepository;

import java.sql.Timestamp;
import java.util.List;

public class SchedulerServiceImpl implements SchedulerService{
    final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }
    public List<Object[]> countVendasByPagamentoPeriodo(Timestamp dataInicial, Timestamp dataFinal) {
        return schedulerRepository.countVendasByPagamentoPeriodo(dataInicial, dataInicial);
    }

    @Override
    public List<Object[]> vendasDetalhadasPorFiialandProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return schedulerRepository.vendasDetalhadasPorFiialandProduto(idFilial, dataInicial, dataInicial);
    }

    @Override
    public Object vendaTotalAtualPorFilial(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return schedulerRepository.vendaTotalAtualPorFilial(idFilial, dataInicial, dataInicial);
    }
}
