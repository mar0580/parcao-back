package com.parcao.service;

import com.parcao.dto.AbastecimentoDTO;
import com.parcao.model.Abastecimento;

import java.util.List;

public interface IAbastecimentoService {

    Abastecimento save(Abastecimento abastecimento);

    Object createAbastecimento(AbastecimentoDTO abastecimentoDto);

    String updateEstoqueFilial(AbastecimentoDTO abastecimentoDto);

    int updateAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountAbastecimento(Long filialId, Long produtoId);

    void adicionaQuantidadeProdutoAbastecimento(int qtd, Long idFilial, Long idProduto);

    List<Abastecimento> getRowCountQuantidadeAbastecimento(Long filialId, Long produtoId, int qtd);
}
