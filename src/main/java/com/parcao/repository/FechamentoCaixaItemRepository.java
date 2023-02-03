package com.parcao.repository;


import com.parcao.services.FechamentoCaixaItemService;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FechamentoCaixaItemRepository implements FechamentoCaixaItemService {
  @PersistenceContext
  private EntityManager entityManager;

  public List<Object[]> selectFechamentoCaixaProdutoDiario(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select fechamento_caixa_itens.id as id, " +
            "fechamento_caixa_itens.inicio as inicio, " +
            "fechamento_caixa_itens.entrada as entrada, " +
            "fechamento_caixa_itens.perda as perda, " +
            "fechamento_caixa_itens.quantidade_final as quantidadeFinal, " +
            "fechamento_caixa.observacao as observacao " +
            "from fechamento_caixa , fechamento_caixa_itens " +
            "where fechamento_caixa.id = fechamento_caixa_itens.fechamento_caixa_id " +
            "and fechamento_caixa.filial_id = :idFilial " +
            "and fechamento_caixa_itens.id = :idProduto " +
            "and fechamento_caixa.date_fechamento_caixa between :dataInicial and :dataFinal");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }

  public List<Object[]> selectFechamentoCaixaProdutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select fechamento_caixa_itens.id as id, " +
            "cast(sum(fechamento_caixa_itens.inicio) as INTEGER) as inicio, " +
            "cast(sum(fechamento_caixa_itens.entrada) as INTEGER) as entrada, " +
            "cast(sum(fechamento_caixa_itens.perda) as INTEGER) as perda, " +
            "cast(sum(fechamento_caixa_itens.quantidade_final) as INTEGER) as quantidadeFinal " +
            "from fechamento_caixa , fechamento_caixa_itens " +
            "where fechamento_caixa.id = fechamento_caixa_itens.fechamento_caixa_id " +
            "and fechamento_caixa.filial_id = :idFilial " +
            "and fechamento_caixa_itens.id = :idProduto " +
            "and fechamento_caixa.date_fechamento_caixa between :dataInicial and :dataFinal group by 1");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }
}