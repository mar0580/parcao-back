package com.parcao.repository;


import com.parcao.models.FechamentoCaixaItem;
import com.parcao.security.services.FechamentoCaixaItemService;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FechamentoCaixaItemRepository implements FechamentoCaixaItemService {
  @PersistenceContext
  private EntityManager entityManager;
  /*
  @Transactional
  @Modifying
  @Query(value = "select fechamento_caixa_itens.* " +
          "from fechamento_caixa , fechamento_caixa_itens " +
          "where fechamento_caixa.id = fechamento_caixa_itens.fechamento_caixa_id " +
          "and fechamento_caixa.filial_id = :filialId " +
          "and fechamento_caixa_itens.id = :produtoId " +
          "and fechamento_caixa.date_fechamento_caixa between :dataInicial and :dataFinal", nativeQuery = true)
  FechamentoCaixaItem selectFechamentoCaixaProduto(Long filialId, Long produtoId, Timestamp dataInicial, Timestamp dataFinal);

  @Transactional
  @Modifying
  @Query(value = "select fechamento_caixa_itens.* " +
          "from fechamento_caixa , fechamento_caixa_itens " +
          "where fechamento_caixa.id = fechamento_caixa_itens.fechamento_caixa_id " +
          "and fechamento_caixa.filial_id = :filialId " +
          "and fechamento_caixa_itens.id = :produtoId " +
          "and fechamento_caixa.date_fechamento_caixa between :dataInicial and :dataFinal", nativeQuery = true)
  List<String> selectFechamentoCaixaProduto_(Long filialId, Long produtoId, Timestamp dataInicial, Timestamp dataFinal);
*/
  public List<Object> selectFechamentoCaixaProduto_(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select fechamento_caixa_itens.id as id, " +
            "fechamento_caixa_itens.inicio as inicio, " +
            "fechamento_caixa_itens.entrada as entrada, " +
            "fechamento_caixa_itens.perda as perda, " +
            "fechamento_caixa_itens.quantidade_final as quantidadeFinal " +
            "from fechamento_caixa , fechamento_caixa_itens " +
            "where fechamento_caixa.id = fechamento_caixa_itens.fechamento_caixa_id " +
            "and fechamento_caixa.filial_id = :idFilial " +
            "and fechamento_caixa_itens.id = :idProduto " +
            "and fechamento_caixa.date_fechamento_caixa between :dataInicial and :dataFinal");
    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object> response = query.getResultList();
    return response;
  }
}