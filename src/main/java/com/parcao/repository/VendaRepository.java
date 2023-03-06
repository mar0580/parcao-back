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
                    "SUM(p.valor_total) as TOTAL_BRUTO_DIA_TODOS_PRODUTOS, " +
                    "SUM(pi.valor_total) as TOTAL_BRUTO_DIA_ITEM, " +
                    "(pi.custo_total/quantidade) as CUSTO_UNITARIO " +
                    "from pedido p, pedido_itens pi " +
                    "where p.id = pi.pedido_id " +
                    "and p.filial_id = :idFilial " +
                    "and pi.id = :idProduto " +
                    "and p.date_pedido between :dataInicial and :dataFinal group by 1,2,6");
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

  public Object selectValorTotalCocoCopoGarrafa(Long idFilial, String descricaoProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select SUM(pi.valor_total) as TOTAL_COCO_COPO_GARRAFA " +
                    "from pedido p, pedido_itens pi, produto po " +
                    "where po.id = pi.id " +
                    "and lower(po.descricao_produto) ilike :descricaoProduto " +
                    "and p.id = pi.pedido_id " +
                    "and p.filial_id = :idFilial " +
                    "and p.date_pedido between :dataInicial and :dataFinal");
    query.setParameter("idFilial", idFilial);
    query.setParameter("descricaoProduto", descricaoProduto.toLowerCase() + "%");
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    return query.getSingleResult();
  }

  public List<Object[]> somatorioTotalBrutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal){
    Query query = (Query) entityManager.createNativeQuery("select po.id, po.descricao_produto,  " +
            " (select (cast(sum(fci.inicio) as INTEGER) + cast(sum(fci.entrada) as INTEGER)) - (cast(sum(fci.perda) as INTEGER) + cast(sum(fci.quantidade_final) as INTEGER)) " +
            "  from fechamento_caixa fc, fechamento_caixa_itens fci " +
            " where fc.id = fci.fechamento_caixa_id " +
            "   and fc.filial_id = :idFilial " +
            "   and fci.id = :idProduto " +
            "   and fc.date_fechamento_caixa between :dataInicial and :dataFinal) as saida, " +
            " (select pi.valor_unitario " +
            "    from pedido p, pedido_itens pi " +
            "   where p.id = pi.pedido_id " +
            "     and p.filial_id = :idFilial " +
            "     and pi.id = :idProduto " +
            "     and p.date_pedido between :dataInicial and :dataFinal) as preco " +
            " from produto po " +
            " where po.id = :idProduto");


    query.setParameter("idFilial", idFilial);
    query.setParameter("idProduto", idProduto);
    query.setParameter("dataInicial", dataInicial);
    query.setParameter("dataFinal", dataFinal);
    List<Object[]> response = query.getResultList();
    return response;
  }

}