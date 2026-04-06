package com.parcao.service;

import com.parcao.model.dto.FilialDTO;
import com.parcao.model.entity.Filial;

import java.util.List;
import java.util.Optional;

public interface IFilialService {

    boolean existsByNomeLocal(String nomeLocal);

    Filial save(Filial filial);

    Filial createFilial(FilialDTO filialDTO);

    Filial updateFilial(Long id, FilialDTO filialDTO);

    List<Filial> findAll();

    boolean existsById(Long id);

    void deleleById(Long id);

    void deleteFilial(Long id);

    Optional<Filial> findById(Long id);

    Filial getFilialById(Long id);

    Optional<Filial> findByNomeLocal(String nomeLocal);
}
