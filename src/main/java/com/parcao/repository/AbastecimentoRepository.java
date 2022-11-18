package com.parcao.repository;

import com.parcao.models.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long>{
}


