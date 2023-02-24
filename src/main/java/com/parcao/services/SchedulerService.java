package com.parcao.services;

import com.parcao.repository.SchedulerRepository;

import java.sql.Timestamp;
import java.util.List;

public interface SchedulerService {
    List<Object[]> countVendasByPagamentoPeriodo(Timestamp dataInicial, Timestamp dataFinal);
}
