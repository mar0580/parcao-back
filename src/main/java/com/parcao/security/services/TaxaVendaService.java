package com.parcao.security.services;

import com.parcao.models.TaxaVenda;

import java.util.List;
import java.util.Optional;

public interface TaxaVendaService {
    boolean existsByNomeTaxa(String nomeTaxa);

    List<TaxaVenda> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<TaxaVenda> findById(Long id);

    TaxaVenda save(TaxaVenda taxaVenda);
}
