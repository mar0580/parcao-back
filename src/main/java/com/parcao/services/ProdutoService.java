package com.parcao.services;

import com.parcao.models.Produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Produto> findById(Long id);

    Produto save(Produto produto);

    int updateProdutoEstoque(Long id, int quantidade);

    void updateProdutoEstoqueGeralSaidaFilial(Long id, int quantidade);

    BigDecimal findCustoProdutoById(Long id);
}
