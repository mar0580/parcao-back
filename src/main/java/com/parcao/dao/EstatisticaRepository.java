package com.parcao.dao;

import com.parcao.services.EstatisticaServiceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EstatisticaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> selectEstatisticaPorTipoPagamento(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery(
                "select " +
                        "tv.nome_taxa, " +
                        "cast(count(p.taxa_venda_id) as INTEGER) as TOTAL_VENDAS_POR_TIPO_PAGAMENTO,  cast(SUM(p.valor_total) as INTEGER) as TOTAL_VENDA " +
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
/*
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
    public List<Object[]> selectTotalVendasDiaria(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery("" +
                "select to_char(p.date_pedido, 'dd-mm') as DIA_MES, " +
                "coalesce(cast(sum(p.valor_total)  as INTEGER), 0) TOTAL_BRUTO_MES_TODAS_VENDAS " +
                "from pedido p " +
                "where p.filial_id = :idFilial " +
                "and p.date_pedido " +
                "between :dataInicial and :dataFinal group by 1");
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }
/*
--definir o lc_time para sua própria sessão
set lc_time  TO 'pt_BR.UTF-8';
*/
    /*
 public List<Object[]> selectTotalVendasMensais(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        Query query = (Query) entityManager.createNativeQuery("" +
                "select to_char(DATE_TRUNC('month',p.date_pedido), 'TMMonth') AS  MES,  " +
                "coalesce(cast(sum(p.valor_total)  as INTEGER), 0) TOTAL_BRUTO_MES_TODAS_VENDAS " +
                "from pedido p " +
                "where p.filial_id = :idFilial " +
                "and p.date_pedido " +
                "between :dataInicial and :dataFinal " +
                "GROUP BY DATE_TRUNC('month',date_pedido)");
        query.setParameter("idFilial", idFilial);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        List<Object[]> response = query.getResultList();
        return response;
    }*/
}
