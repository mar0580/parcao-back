package com.parcao.security.services;

import com.parcao.models.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Produto> findById(Long id);

    Produto save(Produto produto);
}
