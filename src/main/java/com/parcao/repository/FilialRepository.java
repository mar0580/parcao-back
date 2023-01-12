package com.parcao.repository;

import com.parcao.models.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    boolean existsByNomeLocal(String nomeLocal);
    List<Filial> findAll();

    Optional<Filial> findByNomeLocal(String nomeLocal);
}
