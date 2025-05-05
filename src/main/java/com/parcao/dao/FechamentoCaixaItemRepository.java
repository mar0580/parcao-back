package com.parcao.dao;


import com.parcao.services.IFechamentoCaixaItemService;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FechamentoCaixaItemRepository implements IFechamentoCaixaItemService {
  @PersistenceContext
  private EntityManager entityManager;

  public List<Object[]> selectFechamentoCaixaProdutoDiario(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select fci.id as id, " +
            "fci.inicio as inicio, " +
            "fci.entrada as entrada, " +
            "fci.perda as perda, " +
            "fci.quantidade_final as quantidadeFinal, " +
            "(cast(sum(fci.inicio) as INTEGER) + cast(sum(fci.entrada) as INTEGER)) - (cast(sum(fci.perda) as INTEGER) + cast(sum(fci.quantidade_final) as INTEGER))as saida, " +
            "fc.observacao as observacao " +
            "from fechamento_caixa fc, fechamento_caixa_itens fci " +
            "where fc.id = fci.fechamento_caixa_id " +
            "and fc.filial_id = :idFilial " +
            "and fci.id = :idProduto " +
            "and fc.date_fechamento_caixa between :dataInicial and :dataFinal group by 1,2,3,4,5,7");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }

  public List<Object[]> selectFechamentoCaixaProdutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select fci.id as id, " +
            "cast(sum(fci.inicio) as INTEGER) as inicio, " +
            "cast(sum(fci.entrada) as INTEGER) as entrada, " +
            "cast(sum(fci.perda) as INTEGER) as perda, " +
            "cast(sum(fci.quantidade_final) as INTEGER) as quantidadeFinal, " +
            "(cast(sum(fci.inicio) as INTEGER) + cast(sum(fci.entrada) as INTEGER)) - (cast(sum(fci.perda) as INTEGER) + cast(sum(fci.quantidade_final) as INTEGER)) as saida " +
            "from fechamento_caixa fc, fechamento_caixa_itens fci " +
            "where fc.id = fci.fechamento_caixa_id " +
            "and fc.filial_id = :idFilial " +
            "and fci.id = :idProduto " +
            "and fc.date_fechamento_caixa between :dataInicial and :dataFinal group by 1");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }
}