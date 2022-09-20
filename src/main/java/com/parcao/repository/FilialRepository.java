package com.parcao.repository;

import com.parcao.models.Cliente;
import com.parcao.models.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilialRepository extends JpaRepository<Filial, Long> {
    boolean existsByNomeLocal(String nomeLocal);
    List<Filial> findAll();
}
