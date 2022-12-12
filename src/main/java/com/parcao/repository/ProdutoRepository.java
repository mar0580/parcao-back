package com.parcao.repository;

import com.parcao.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();
    @Transactional
    @Modifying
    @Query("update Produto p set p.quantidade = (p.quantidade - :quantidade) where p.id = :id")
    int updateProdutoEstoque(@Param(value = "id") Long id, @Param(value = "quantidade") int quantidade);
}
