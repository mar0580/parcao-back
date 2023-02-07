package com.parcao.repository;

import com.parcao.services.VendaService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class VendaRepository implements VendaService {
  @PersistenceContext
  private EntityManager entityManager;

  public Object selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select SUM(p.valor_total) as total " +
            "from pedido p, pedido_itens pi " +
            "where p.id = pi.pedido_id " +
            "and p.filial_id = :idFilial " +
            "and pi.id = :idProduto " +
            "and p.date_pedido between :dataInicial and :dataFinal");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    return query.getSingleResult();
  }

}