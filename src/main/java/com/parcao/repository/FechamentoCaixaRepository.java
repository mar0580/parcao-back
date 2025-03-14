package com.parcao.repository;

import com.parcao.model.entity.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> { }
