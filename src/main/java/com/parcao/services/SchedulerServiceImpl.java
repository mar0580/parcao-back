package com.parcao.services;

import com.parcao.repository.SchedulerRepository;

import java.sql.Timestamp;
import java.util.List;

public class SchedulerServiceImpl implements SchedulerService{
    final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }
    public List<Object[]> countVendasByPagamentoPeriodo(Timestamp dataInicial, Timestamp dataFinal) {

        List<Object[]> objects = schedulerRepository.countVendasByPagamentoPeriodo(dataInicial, dataInicial);
        return objects;
    }
}
