package com.parcao.services;

import com.parcao.models.Abastecimento;

import java.util.List;
import java.util.Optional;

public interface AbastecimentoService {

    Abastecimento save(Abastecimento abastecimento);

    void updateAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountAbastecimento(Long filialId, Long produtoId);

    void adicionaQuantidadeProdutoAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountQuantidadeAbastecimento(Long filialId, Long produtoId, int qtd);
}
