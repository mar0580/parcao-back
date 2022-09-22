package com.parcao.security.services;

import com.parcao.models.Filial;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FilialService {

    public boolean existsByNomeLocal(String nomeLocal);

    public Filial save(Filial filial);

    public Object findAll(Pageable pageable);

    public boolean existsById(Long id);

    public void deleleById(Long id);

    public Optional<Filial> findById(Long id);
}
