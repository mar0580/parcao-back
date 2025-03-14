package com.parcao.services;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.model.dto.ProdutoDto;
import com.parcao.model.entity.Produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);

    Produto findById(Long id);

    Produto save(ProdutoDto produtoDto) throws ProdutoJaCadastradoException;

    Produto atualizarProduto(Long id, ProdutoDto produtoDto);

    int updateProdutoEstoque(Long id, int quantidade);

    BigDecimal findCustoProdutoById(Long id);
}
