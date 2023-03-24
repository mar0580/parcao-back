package com.parcao.repository;

import com.parcao.services.EstatisticaService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EstatisticaRepository implements EstatisticaService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> selectEstatisticaPorTipoPagamento(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery(
                "select " +
                        "tv.nome_taxa, " +
                        "cast(count(p.taxa_venda_id) as INTEGER) as TOTAL_VENDAS_POR_TIPO_PAGAMENTO " +
                        "from pedido p, taxa_venda tv " +
                        "where p.filial_id = :idFilial " +
                        "and p.taxa_venda_id = tv.id " +
                        "and p.date_pedido between :dataInicial and :dataFinal group by 1");
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }
}
