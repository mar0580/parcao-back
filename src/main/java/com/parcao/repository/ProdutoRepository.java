package com.parcao.repository;

import com.parcao.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByDescricaoProduto(String descricaoProduto);

    @Transactional
    @Modifying
    @Query("UPDATE Produto p SET p.quantidade = :quantidade WHERE p.id = :id")
    int atualizarEstoqueProduto(@Param(value = "id") Long id, @Param(value = "quantidade") int quantidade);

    @Query("SELECT p.valorCustoUnitario FROM Produto p WHERE p.id = :id")
    BigDecimal buscarCustoProdutoPorId(@Param(value = "id") Long id);
}
