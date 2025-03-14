package com.parcao.repository;

import com.parcao.model.entity.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    boolean existsByNomeLocal(String nomeLocal);

    Optional<Filial> findByNomeLocal(String nomeLocal);
}
