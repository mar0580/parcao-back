package com.parcao.security.services;

import com.parcao.models.Cliente;
import com.parcao.models.Filial;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FilialService {

    public boolean existsByNomeLocal(String nomeLocal);

    public Filial save(Filial filial);

    List<Filial> findAll();

    public boolean existsById(Long id);

    public void deleleById(Long id);

    public Optional<Filial> findById(Long id);
}
