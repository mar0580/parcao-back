package com.parcao.security.services;

import com.parcao.models.Abastecimento;

import java.util.Optional;

public interface AbastecimentoService {

    Abastecimento save(Abastecimento abastecimento);

    Optional<Abastecimento> findAbastecimentoByIdFilial(Long id);

    Integer updateAbastecimento(int qtd, Long idFilial, Long idProduto);
}
