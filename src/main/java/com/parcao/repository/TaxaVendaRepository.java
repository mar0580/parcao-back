package com.parcao.repository;

import com.parcao.model.entity.TaxaVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaxaVendaRepository extends JpaRepository<TaxaVenda, Long> {

    boolean existsByNomeTaxa(String nomeTaxa);
}
