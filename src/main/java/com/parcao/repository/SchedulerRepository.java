package com.parcao.repository;

import com.parcao.services.SchedulerService;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SchedulerRepository implements SchedulerService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> countVendasByPagamentoPeriodo(Timestamp dataInicial, Timestamp dataFinal) {
        Query query = (Query) entityManager.createNativeQuery("select p.taxa_venda_id, " +
                "tv.nome_taxa, " +
                "count(p.taxa_venda_id) as quantidade, " +
                "cast(SUM(p.valor_total) as INTEGER) as total_venda " +
                "from pedido p, taxa_venda tv " +
                "where tv.id = p.taxa_venda_id and p.date_pedido between :dataInicial and :dataFinal group by 1,2");
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }

}
