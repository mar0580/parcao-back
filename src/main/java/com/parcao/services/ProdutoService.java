package com.parcao.services;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.model.dto.ProdutoDTO;
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

    Produto save(ProdutoDTO ProdutoDTO) throws ProdutoJaCadastradoException;

    Produto atualizarProduto(Long id, ProdutoDTO ProdutoDTO);

    int updateProdutoEstoque(Long id, int quantidade);

    BigDecimal findCustoProdutoById(Long id);
}
