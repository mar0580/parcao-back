package com.parcao.services;

import com.parcao.model.entity.Abastecimento;

import java.util.List;

public interface AbastecimentoService {

    Abastecimento save(Abastecimento abastecimento);

    int updateAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountAbastecimento(Long filialId, Long produtoId);

    void adicionaQuantidadeProdutoAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountQuantidadeAbastecimento(Long filialId, Long produtoId, int qtd);
}
