package com.parcao.repository;

import com.parcao.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();
    @Transactional
    @Modifying
    @Query("update Produto p set p.quantidade = :qtd where p.id = :id")
    int updateProdutoEstoque(@Param(value = "id") Long id, @Param(value = "qtd") int qtd);

    @Query(value = "select valor_custo_unitario from  produto where produto.id = :id", nativeQuery = true)
    BigDecimal findCustoProdutoById(@Param(value = "id") Long id);

}
