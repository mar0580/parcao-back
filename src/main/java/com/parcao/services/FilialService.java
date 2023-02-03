package com.parcao.services;

import com.parcao.models.Filial;

import java.util.List;
import java.util.Optional;

public interface FilialService {

    boolean existsByNomeLocal(String nomeLocal);

    Filial save(Filial filial);

    List<Filial> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Filial> findById(Long id);

    Optional<Filial> findByNomeLocal(String nomeLocal);
}
