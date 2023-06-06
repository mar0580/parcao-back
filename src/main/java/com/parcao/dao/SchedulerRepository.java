package com.parcao.dao;

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
    public Object vendaTotalAtualPorFilial(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        Query query = (Query) entityManager.createNativeQuery("select sum(pi.valor_total) as VALOR_TOTAL " +
                "  from pedido p, pedido_itens pi " +
                "where p.id = pi.pedido_id " +
                "  and p.filial_id = :idFilial " +
                "  and p.date_pedido between :dataInicial and :dataFinal");
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        return query.getSingleResult();
    }

    public List<Object[]> vendasDetalhadasPorFiialandProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        Query query = (Query) entityManager.createNativeQuery("select " +
                " pi.nome_produto as produto, sum(pi.quantidade) as quantidade, sum(pi.valor_total) as valor_total " +
                "  from filial f,pedido p, pedido_itens pi " +
                " where p.id = pi.pedido_id " +
                "   and p.filial_id = f.id " +
                "   and p.filial_id = :idFilial " +
                "   and p.date_pedido between :dataInicial and :dataFinal group by 1");
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }
}
