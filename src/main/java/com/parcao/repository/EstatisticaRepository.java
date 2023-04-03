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

    public List<Object[]> selectPerdasPorMes(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery(" " +
                "select to_char(DATE_TRUNC('month',fc.date_fechamento_caixa), 'TMMonth') as MES, " +
                "coalesce(cast(sum(fci.perda) as INTEGER), 0) as PERDA " +
                "from fechamento_caixa fc, fechamento_caixa_itens fci " +
                "where fc.id = fci.fechamento_caixa_id " +
                "and fc.filial_id = :idFilial " +
                "and fc.date_fechamento_caixa between :dataInicial and :dataFinal " +
                "group by DATE_TRUNC('month',fc.date_fechamento_caixa)" );
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }

    public List<Object[]> selectPerdasPorProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery("" +
                "select (select p.descricao_produto from produto p where p.id = fci.id) as NOME_PRODUTO, " +
                "       coalesce(cast(sum(fci.perda) as INTEGER), 0) as PERDA " +
                "  from fechamento_caixa fc, fechamento_caixa_itens fci " +
                " where fc.id = fci.fechamento_caixa_id " +
                "   and fc.filial_id = :idFilial " +
                "   and fc.date_fechamento_caixa between :dataInicial and :dataFinal group by 1" );
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }
/*



--relatorio de perdas por produto
select (select p.descricao_produto from produto p where p.id = fci.id) as NOME_PRODUTO,
       coalesce(cast(sum(fci.perda) as INTEGER), 0) as as PERDA
  from fechamento_caixa fc, fechamento_caixa_itens fci
 where fc.id = fci.fechamento_caixa_id
   and fc.filial_id = 1
   and fc.date_fechamento_caixa between '2023-03-01 00:00:00.000' and '2023-03-30 23:59:59.999' group by 1;

--total de vendas por dia
select
      to_char(p.date_pedido, 'dd-mm') as DIA_MES,
      coalesce(SUM(p.valor_total), 0) as as TOTAL_BRUTO_DIA_TODOS_PRODUTOS
  from pedido p
 where p.filial_id = 1
   and p.date_pedido between '2023-03-01 00:00:00.000' and '2023-03-30 23:59:59.999' group by 1;

--definir o lc_time para sua própria sessão
set lc_time  TO 'pt_BR.UTF-8';

--total de venda por mês
select to_char(DATE_TRUNC('month',p.date_pedido), 'TMMonth') AS  MES,
       SUM(p.valor_total) as TOTAL_BRUTO_MES_TODAS_VENDAS
  from pedido p
 where p.filial_id = 1
   and p.date_pedido between '2023-03-01 00:00:00.000' and '2023-03-30 23:59:59.999'
GROUP BY DATE_TRUNC('month',date_pedido);



     */
}
