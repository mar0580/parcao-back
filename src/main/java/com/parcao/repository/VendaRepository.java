package com.parcao.repository;

import com.parcao.services.VendaService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class VendaRepository implements VendaService {
  @PersistenceContext
  private EntityManager entityManager;

  public List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery(
            "select " +
                    "pi.id, " +
                    "pi.valor_unitario as VALOR_UNITARIO, " +
                    "SUM(pi.custo_total) as CUSTO_TOTAL_UNITARIO, " +
                    "SUM(p.valor_total) as TOTAL_BRUTO_DIA, " +
                    "(pi.custo_total/quantidade) as CUSTO_UNITARIO " +
                    "from pedido p, pedido_itens pi " +
                    "where p.id = pi.pedido_id " +
                    "and p.filial_id = :idFilial " +
                    "and pi.id = :idProduto " +
                    "and p.date_pedido between :dataInicial and :dataFinal group by 1,2");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }

  public Object selectValorBrutoPeriodo(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select sum(pi.valor_total) as VALOR_TOTAL " +
            "from pedido p, pedido_itens pi " +
            "where p.id = pi.pedido_id " +
            "and p.filial_id = :idFilial " +
            "and p.date_pedido between :dataInicial and :dataFinal");
    query.setParameter("idFilial", idFilial);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    return query.getSingleResult();
  }

}