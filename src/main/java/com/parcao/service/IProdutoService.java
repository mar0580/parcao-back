package com.parcao.service;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.dto.ProdutoDTO;
import com.parcao.model.Produto;

import java.math.BigDecimal;
import java.util.List;

public interface IProdutoService {
    boolean existsByDescricaoProduto(String descricaoProduto);

    List<Produto> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);

    Produto findById(Long id);

    Produto save(ProdutoDTO ProdutoDTO) throws ProdutoJaCadastradoException;

    Produto atualizarProduto(Long id, ProdutoDTO ProdutoDTO);

    int updateProdutoEstoque(Long id, int quantidade);

    BigDecimal findCustoProdutoById(Long id);
}
