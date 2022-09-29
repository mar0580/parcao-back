package com.parcao.security.services;

import com.parcao.models.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    public boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();

    public boolean existsById(Long id);

    public void deleleById(Long id);

    public Optional<Produto> findById(Long id);

    public Produto save(Produto produto);
}
