package com.parcao.repository;

import com.parcao.models.Filial;
import com.parcao.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();
}
