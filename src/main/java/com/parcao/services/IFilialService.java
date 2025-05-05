package com.parcao.services;

import com.parcao.model.entity.Filial;

import java.util.List;
import java.util.Optional;

public interface IFilialService {

    boolean existsByNomeLocal(String nomeLocal);

    Filial save(Filial filial);

    List<Filial> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Filial> findById(Long id);

    Optional<Filial> findByNomeLocal(String nomeLocal);
}
