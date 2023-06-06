package com.parcao.dao;

import com.parcao.models.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {


}
